package com.example.hello.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hello.R;
import com.example.hello.data.DBOpenHelper;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 欢迎界面*/
public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private int recLen = 3;//跳过倒计时提示3秒
    private TextView tv;
    Timer timer = new Timer();
    private Handler handler;
    private Runnable runnable;
    private DBOpenHelper dbOpenHelper;
    private String onlineUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //使图片全屏
        dbOpenHelper = new DBOpenHelper(this);
        onlineUser = dbOpenHelper.getUser();
        initView();
        //延时进入
        longTimeEnter();

    }

    /**
     *安卓中无法直接通过子线程来进行UI更新操作
     * 异步消息处理机制Handler
     * Handler:
     * 1 处理的消息对象就是Message,理解为要传递的消息数据的封装对象
     * Message what : 标记,用来区分多个消息
     * Message arg1,arg2 : 用来传递int类型的数据
     * Message obj : 可以传递任何类型的对象(Object)
     */
    private void longTimeEnter() {
        TimerTask task = new TimerTask() {
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                // UI thread
                runOnUiThread(() -> {
                    tv.setText("跳过 " + recLen);
                    recLen--;
                    if (recLen < 0) {
                        timer.cancel();
                        tv.setVisibility(View.GONE);//倒计时到0隐藏字体
                    }
                });
            }
        };
        timer.schedule(task, 1000, 1000);//等待时间一秒，停顿时间一秒
        /*
         * 正常情况下不点击跳过
         * 延时3秒进入
         */
        handler = new Handler();
        //计时结束后跳转到其他界面
        handler.postDelayed(runnable = () -> {
            boolean isLogin = false;
            if (onlineUser != null) {
                isLogin = true;
            }
            Intent intent;
            if (isLogin) {
                //从闪屏界面跳转到首界面
                intent = new Intent(WelcomeActivity.this, MainActivity.class);
            } else {
                intent = new Intent(WelcomeActivity.this, loginActivity.class);
            }
            startActivity(intent);
            //添加finish方法在任务栈中销毁倒计时界面，使新开界面在回退时直接退出而不是再次返回该界面
            finish();
        }, 3000);//延迟3S后发送handler信息

    }

    private void initView() {
        tv = findViewById(R.id.tv);//跳过
        tv.setOnClickListener(this);//跳过监听
    }


    /**
     * 点击跳过
     */
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv) {
            boolean isLogin = false;
            dbOpenHelper = new DBOpenHelper(this);
            String onlineUser = dbOpenHelper.getUser();
            if (onlineUser != null) {
                isLogin = true;
            }
            Intent intent;
            if (isLogin) {
                //从闪屏界面跳转到首界面
                intent = new Intent(WelcomeActivity.this, MainActivity.class);
            } else {
                intent = new Intent(WelcomeActivity.this, loginActivity.class);
            }
            startActivity(intent);
            finish();
            if (runnable != null) {
                handler.removeCallbacks(runnable);
            }
        }
    }

}



 