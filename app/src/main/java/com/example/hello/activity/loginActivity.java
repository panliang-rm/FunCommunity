package com.example.hello.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hello.R;
import com.example.hello.data.DBOpenHelper;
import com.example.hello.data.User;

import java.util.ArrayList;

public class loginActivity extends AppCompatActivity implements View.OnClickListener {


    private DBOpenHelper mDBOpenHelper;
    private EditText userName;
    private EditText passWord;
    private int temp=0;//简单判断用户名和密码的标记

    /**
     * 声明自己写的 DBOpenHelper 对象
     * DBOpenHelper(extends SQLiteOpenHelper) 主要用来
     * 创建数据表
     * 然后再进行数据表的增、删、改、查操作
*/    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView(); //把视图层View也就是layout 与 控制层 Java 结合起来了
        mDBOpenHelper = new DBOpenHelper(this); //实例化 DBOpenHelper，待会进行登录验证的时候要用来进行数据查询



    }

    //控件多，用方法独立出来较好
    private void initView() {
        // 初始化控件
        Button loginactivityLogin = findViewById(R.id.bt_loginactivity_login);
        TextView activityRegister = findViewById(R.id.tv_loginactivity_register);
        userName = findViewById(R.id.et_loginactivity_username);
        passWord = findViewById(R.id.et_loginactivity_password);

        // 设置点击事件监听器
        loginactivityLogin.setOnClickListener(this);
        activityRegister.setOnClickListener(this);
        //设置密码不可见
        passWord.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        switch (view.getId()) {
            // 跳转到注册界面
            case R.id.tv_loginactivity_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.bt_loginactivity_login:
                //获取edittext上的用户名和密码
                String name = userName.getText().toString().trim();
                String password = passWord.getText().toString().trim();
                //进行匹配验证,先判断一下用户名密码是否为空
                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)) {
                    ArrayList<User> data = mDBOpenHelper.getAllData();
                    boolean match = false;
                    //再进而for循环判断是否与数据库中的数据相匹配
                    for (int i = 0; i < data.size(); i++) {
                        User user = data.get(i);
                        //一旦匹配，立即将match = true；break； 否则 一直匹配到结束 match = false；
                        if (name.equals(user.getName()) && password.equals(user.getPassword())) {
                            match = true;
                            break;
                        } else if (name.equals(user.getName())){
                            temp =1;
                            match = false;
                        } else {
                            match = false;
                        }
                    }
                    if (match) {
                        //更改登录状态：online上线
                        mDBOpenHelper.update(name);
                        mDBOpenHelper.updatatuwenlogin(name);
                        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                        //登录成功之后，进行页面跳转：
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();//销毁此Activity
                    } else {
                        if ( temp == 1 ) {
                            passWord.setText("");
                            Toast.makeText(this, "密码不正确，请重新输入", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(this, "没有此用户名，请重新输入", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "请输入你的用户名或密码", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}