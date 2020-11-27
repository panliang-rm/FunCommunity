package com.example.hello.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hello.R;
import com.example.hello.data.DBOpenHelper;

/**
 * 退出登录
 * 修改密码的界面*/
public class SetActivity extends AppCompatActivity implements View.OnClickListener{

    //实现访问数据库数据的实例
    private DBOpenHelper dbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        initView();
        dbOpenHelper = new DBOpenHelper(this);
    }

    private void initView() {
        // 初始化控件
        LinearLayout changePassword = findViewById(R.id.change_password);
        LinearLayout about = findViewById(R.id.about_set);
        ImageView back = findViewById(R.id.back);
        LinearLayout toback = findViewById(R.id.toback);
        // 设置点击事件监听器
        changePassword.setOnClickListener(this);
        about.setOnClickListener(this);
        back.setOnClickListener(this);
        toback.setOnClickListener(this);
    }


    //每个点击事件都使用了对话框
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.change_password :
                EditText editText = new EditText(this);
                editText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                editText.setHint("数字/字符");
                //添加取消
                AlertDialog alertDialog = new AlertDialog.Builder(v.getContext())
                        .setTitle("请输入")
                        .setView(editText)
                        .setNegativeButton("取消", (dialogInterface, i) -> {
                        })
                        .setPositiveButton("确定", null)
                        .create();
                alertDialog.setCancelable(false);
                alertDialog.show();
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v1 -> {
                    String newpassword = editText.getText().toString().trim();
                    if (newpassword.equals("")) {
                        Toast.makeText(v1.getContext(), "密码为空,请重新输入", Toast.LENGTH_SHORT).show();
                    } else {
                        if (!newpassword.equals(dbOpenHelper.getPassWord())) {
                            dbOpenHelper.updatePassWord(newpassword);
                            Toast.makeText(v1.getContext(), "修改成功", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(v1.getContext(), "密码重复，请重新输入", Toast.LENGTH_SHORT).show();
                            editText.setText("");
                        }
                    }
                });
                break;
            case R.id.about_set:
                //添加取消
                AlertDialog alertDialog1 = new AlertDialog.Builder(SetActivity.this)
                        .setTitle("极光三轮考核项目")
                        .setMessage("Create by：panliang")
                        .setNegativeButton("确定", (dialogInterface, i) -> {
                        })
                        .setIcon(R.mipmap.ic_launcher_round)
                        .create();
                alertDialog1.setCancelable(false);
                alertDialog1.show();
                break;
            case R.id.back:
                finish();
                break;
            case R.id.toback:
                //添加取消
                AlertDialog alertDialog2 = new AlertDialog.Builder(SetActivity.this)
                        .setTitle("退出登录")
                        .setNegativeButton("取消", (dialogInterface, i) -> {
                        })
                        .setPositiveButton("确定", (dialog, which) -> {
                            Intent intent = new Intent(SetActivity.this, loginActivity.class);
                            dbOpenHelper.updataZero(dbOpenHelper.getUser());
                            dbOpenHelper.updatatuwenzero();
                            startActivity(intent);
                            finish();
                        })
                        .create();
                alertDialog2.setCancelable(false);
                alertDialog2.show();
                break;

        }
    }


}

