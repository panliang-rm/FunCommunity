package com.example.hello.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hello.R;
import com.example.hello.data.DBOpenHelper;
import com.example.hello.data.User;

import java.util.ArrayList;

/**
 * 注册界面
 * 此类 implements View.OnClickListener 之后，
 * 就可以把onClick事件写到onCreate()方法之外
 * 这样，onCreate()方法中的代码就不会显得很冗余
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private DBOpenHelper mDBOpenHelper;

    private EditText registeractivityUsername;
    private EditText registeractivityPassword2;
    private EditText registeractivityPassword1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();

        mDBOpenHelper = new DBOpenHelper(this);


    }

    private void initView(){
        Button registeractivityRegister = findViewById(R.id.bt_registeractivity_register);

        ImageView registeractivityBack = findViewById(R.id.iv_registeractivity_back);

        registeractivityUsername = findViewById(R.id.et_registeractivity_username);
        registeractivityPassword1 = findViewById(R.id.et_registeractivity_password1);
        registeractivityPassword2 = findViewById(R.id.et_registeractivity_password2);

        registeractivityPassword1.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        registeractivityPassword2.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
        /*
         * 注册页面能点击的两个地方
         * top处返回箭头、注册按钮
         */
        registeractivityBack.setOnClickListener(this);
        registeractivityRegister.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_registeractivity_back: //返回登录页面
                Intent intent1 = new Intent(this, loginActivity.class);
                startActivity(intent1);
                finish();
                break;
            case R.id.bt_registeractivity_register:    //注册按钮
                //获取用户输入的用户名、密码、验证码
                String username = registeractivityUsername.getText().toString().trim();
                String password1 = registeractivityPassword1.getText().toString().trim();
                String password2 = registeractivityPassword2.getText().toString().trim();


            //注册验证
                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password1) && !TextUtils.isEmpty(password2) ) {
                    if (password1.equals(password2) ) {
                        ArrayList<User> users = mDBOpenHelper.getAllData();
                        //再进而for循环判断是否与数据库中的数据相匹配
                        for (int i = 0; i < users.size(); i++) {
                            User user = users.get(i);
                            if (username.equals(user.getName())){
                                Toast.makeText(this, "此用户名太受欢迎，请换一个(*^_^*)", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        //将用户名和密码加入到数据库中

                        mDBOpenHelper.add(username, password2,"1");
                        Intent intent2 = new Intent(this, MainActivity.class);
                        startActivity(intent2);
                        finish();
                        Toast.makeText(this,  "验证通过，注册成功(*^_^*)", Toast.LENGTH_SHORT).show();
                    } else {
                        registeractivityPassword1.setText("");
                        registeractivityPassword2.setText("");
                        Toast.makeText(this, "两次密码不一致(*^_^*)", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "未完善信息，注册失败(*^_^*)", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
