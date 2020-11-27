package com.example.hello.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.hello.R;
import com.example.hello.data.CommentsBean;
import com.example.hello.data.DBOpenHelper;
import com.example.hello.data.Like;
import com.example.hello.data.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 修改个人信息界面
 */
public class PersonActivity extends AppCompatActivity {

    //相册请求码
    private static final int ALBUM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;

    private ImageView imageView;
    private TextView finish;
    private ImageView headphoto;
    private EditText editText1;
    private EditText editText2;
    private DBOpenHelper dbOpenHelper;
    private RadioGroup radioGroup;
    private String headphotopath;
    private String beijingpath;
    private ImageView beijingban;
    private String name;
    private int i = -1;//判断选择的图片用于头像还是背景板

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        initView();
        dbOpenHelper = new DBOpenHelper(this);
        //进入编辑资料界面，显示图片
        if (null != dbOpenHelper.getHeadPhoto()) {
            Glide.with(this).load(new File(dbOpenHelper.getHeadPhoto())).into(headphoto);
          }
        if (null!= dbOpenHelper.getBeiJing()) {
            Glide.with(this).load(new File(dbOpenHelper.getBeiJing())).into(beijingban);
        }
         onClickData();

    }

    private void initView() {
        imageView = findViewById(R.id.back);
        finish = findViewById(R.id.forward);
        headphoto = findViewById(R.id.iv_head_icon);
        editText1 = findViewById(R.id.tv_user_name);
        editText2 = findViewById(R.id.qianming);
        radioGroup = findViewById(R.id.rdg_gender);
        beijingban = findViewById(R.id.beginning_person);
    }

    private void onClickData() {

        headphoto.setOnClickListener(v -> {
                i = 1;
                showTypeDialog();
        });

        //如果点击back则结束活动
       imageView.setOnClickListener(v -> finish());

       //性别选择
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbtn_male);

        });
        //保存
        finish.setOnClickListener(v -> {
            ArrayList<User> data = dbOpenHelper.getAllData();
             name = editText1.getText().toString().trim();
            String qianming = editText2.getText().toString().trim();
            String username = dbOpenHelper.getUser();
            // String sex = radioButton.getText().toString();
            if (headphotopath != null && name.length() == 0 && qianming.length() == 0 && beijingpath == null) {
                dbOpenHelper.updataHeadPhoto(headphotopath);
                dbOpenHelper.updatatuwenHeadPhoto(headphotopath);
                finish();
            } else if (headphotopath == null && name != null && qianming.length() == 0&& beijingpath == null) {
                boolean match = false;
                for (int i = 0; i < data.size(); i++) {
                    User user = data.get(i);
                    if (name.equals(user.getName())) {
                        Toast.makeText(this, "用户名重复，请修改", Toast.LENGTH_SHORT).show();
                        match = false;
                        break;
                    } else
                        match = true;
                }
                if (match) {
                    changeName();
                    dbOpenHelper.updataName(name);
                    dbOpenHelper.updatatuwenName(name);
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else if (headphotopath == null && name.length() == 0 && qianming.length() != 0&& beijingpath == null) {

                dbOpenHelper.addQianMing(qianming,username);
                Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                finish();
            } else if (headphotopath != null && name.length() != 0 && qianming.length() == 0&& beijingpath == null) {
                dbOpenHelper.updataHeadPhoto(headphotopath);
                dbOpenHelper.updatatuwenHeadPhoto(headphotopath);
                boolean match = false;
                for (int i = 0; i < data.size(); i++) {
                    User user = data.get(i);
                    if (name.equals(user.getName())) {
                        Toast.makeText(this, "用户名重复，请修改", Toast.LENGTH_SHORT).show();
                        match = false;
                        break;
                    } else
                        match = true;
                }
                if (match) {
                    changeName();
                    dbOpenHelper.updataName(name);
                    dbOpenHelper.updatatuwenName(name);
                    Toast.makeText(this, "保存成功1", Toast.LENGTH_SHORT).show();
                    finish();
                }
                Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                finish();
            } else if (headphotopath != null && name.length() == 0 && qianming.length() != 0&& beijingpath == null) {
                changeName();
                dbOpenHelper.updataHeadPhoto(headphotopath);
                dbOpenHelper.updatatuwenHeadPhoto(headphotopath);
                dbOpenHelper.addQianMing(qianming,username);
                Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                finish();
            } else if (headphotopath == null && name.length() != 0 && qianming.length() != 0&& beijingpath == null) {
                boolean match = false;
                for (int i = 0; i < data.size(); i++) {
                    User user = data.get(i);
                    if (name.equals(user.getName())) {
                        Toast.makeText(this, "用户名重复，请修改", Toast.LENGTH_SHORT).show();
                        match = false;
                        break;
                    } else
                        match = true;
                }
                if (match) {
                    dbOpenHelper.addQianMing(qianming,username);
                    changeName();
                    dbOpenHelper.updataName(name);
                    dbOpenHelper.updatatuwenName(name);
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }

            } else  if (headphotopath == null && name.length() == 0 && qianming.length() == 0&& beijingpath == null) {
                Toast.makeText(this,"用户名为空，无法保存",Toast.LENGTH_SHORT).show();
            } else if (headphotopath == null && name.length() == 0 && qianming.length() == 0 && beijingpath != null) {
                dbOpenHelper.updateBeiJing(beijingpath);
                Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show();
                finish();
            } else if (headphotopath != null && name.length() == 0 && qianming.length() == 0 && beijingpath != null) {
                dbOpenHelper.updataHeadPhoto(headphotopath);
                dbOpenHelper.updatatuwenHeadPhoto(headphotopath);
                dbOpenHelper.updateBeiJing(beijingpath);
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
            } else if (headphotopath == null && name.length() != 0 && qianming.length() == 0 && beijingpath != null) {
                boolean match = false;
                for (int i = 0; i < data.size(); i++) {
                    User user = data.get(i);
                    if (name.equals(user.getName())) {
                        Toast.makeText(this, "用户名重复，请修改", Toast.LENGTH_SHORT).show();
                        match = false;
                        break;
                    } else
                        match = true;
                }
                if (match) {
                    dbOpenHelper.updateBeiJing(beijingpath);
                    changeName();
                    dbOpenHelper.updataName(name);
                    dbOpenHelper.updatatuwenName(name);
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else if (headphotopath == null && name.length() == 0 && qianming.length() != 0 && beijingpath != null) {
                dbOpenHelper.addQianMing(qianming,username);
                dbOpenHelper.updateBeiJing(beijingpath);
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
            } else if (headphotopath != null && name.length() != 0 && qianming.length() == 0 && beijingpath != null) {
                dbOpenHelper.updataHeadPhoto(headphotopath);
                boolean match = false;
                for (int i = 0; i < data.size(); i++) {
                    User user = data.get(i);
                    if (name.equals(user.getName())) {
                        Toast.makeText(this, "用户名重复，请修改", Toast.LENGTH_SHORT).show();
                        match = false;
                        break;
                    } else
                        match = true;
                }
                if (match) {
                    dbOpenHelper.updateBeiJing(beijingpath);
                    changeName();
                    dbOpenHelper.updataName(name);
                    dbOpenHelper.updatatuwenName(name);
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else if (headphotopath != null && name.length() == 0 && qianming.length() != 0 && beijingpath != null) {
                dbOpenHelper.updateBeiJing(beijingpath);
                dbOpenHelper.updataHeadPhoto(headphotopath);
                dbOpenHelper.updatatuwenHeadPhoto(headphotopath);
                dbOpenHelper.addQianMing(qianming,username);
                changeName();
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
            } else if (headphotopath == null && name.length() != 0 && qianming.length() != 0 && beijingpath != null) {
                dbOpenHelper.addQianMing(qianming,username);
                boolean match = false;
                for (int i = 0; i < data.size(); i++) {
                    User user = data.get(i);
                    if (name.equals(user.getName())) {
                        Toast.makeText(this, "用户名重复，请修改", Toast.LENGTH_SHORT).show();
                        match = false;
                        break;
                    } else
                        match = true;
                }
                if (match) {
                    dbOpenHelper.updateBeiJing(beijingpath);
                    changeName();
                    dbOpenHelper.updataName(name);
                    dbOpenHelper.updatatuwenName(name);
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else{
                boolean match = false;
                for (int i = 0; i < data.size(); i++) {
                    User user = data.get(i);
                    if (name.equals(user.getName())) {
                        Toast.makeText(this, "用户名重复，请修改", Toast.LENGTH_SHORT).show();
                        match = false;
                        break;
                    } else
                        match = true;
                }
                if (match) {
                    dbOpenHelper.updataHeadPhoto(headphotopath);
                    dbOpenHelper.updatatuwenHeadPhoto(headphotopath);
                    dbOpenHelper.addQianMing(qianming,username);
                    dbOpenHelper.updateBeiJing(beijingpath);
                    changeName();
                    dbOpenHelper.updataName(name);
                    dbOpenHelper.updatatuwenName(name);
                    Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

        beijingban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 0;
                showTypeDialog();
            }
        });


    }

    //对于点赞和评论已经保存到数据库的数据，需要取出之后再次修改，后再保存
    private void changeName() {
        int id = 0;
        Type liketype1 = new TypeToken<List<Like>>() {
        }.getType();
        List<String> stringList = dbOpenHelper.getAllLike();
        if (stringList != null) {
            for (int j = 0; j < stringList.size(); j++) {
                List<Like> likeList = new Gson().fromJson(stringList.get(j), liketype1);
                if (likeList != null) {
                    for (int n = 0; n < likeList.size(); n++) {
                        if (likeList.get(n).getUser().equals(dbOpenHelper.getUser())) {
                            likeList.get(n).setUser(name);
                            id = likeList.get(n).getId();
                        }
                    }

                    String string = new Gson().toJson(likeList);
                    dbOpenHelper.updateLike(string, id);
                }
            }
        }

        Type liketype2 = new TypeToken<List<CommentsBean>>() {}.getType();
        List<String> stringList1 = dbOpenHelper.getCommentList();
        if (stringList1 != null) {
            for (int a = 0; a < stringList1.size(); a++) {
                List<CommentsBean> commentsBeanList = new Gson().fromJson(stringList1.get(a), liketype2);
                if (commentsBeanList != null) {
                    for (int b = 0; b < commentsBeanList.size(); b++) {
                        if (commentsBeanList.get(b).getCommentsUser().getUserName().equals(dbOpenHelper.getUser())) {
                            commentsBeanList.get(b).getCommentsUser().setUserName(name);
                            id = commentsBeanList.get(b).getId();
                        }
                        if (commentsBeanList.get(b).getReplyUser() !=null) {
                        if (commentsBeanList.get(b).getReplyUser().getUserName().equals(dbOpenHelper.getUser())) {
                            commentsBeanList.get(b).getReplyUser().setUserName(name);
                        }
                        }
                    }
                    String string = new Gson().toJson(commentsBeanList);
                    dbOpenHelper.addcomments(string, id);
                }
            }
        }

    }


    private void showTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_select_photo, null);
        TextView tv_select_gallery = view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = view.findViewById(R.id.tv_select_camera);
        //调用相册
        tv_select_gallery.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, ALBUM_REQUEST_CODE);
            dialog.dismiss();
        });
        // 调用照相机
        tv_select_camera.setOnClickListener(v -> {
          /*  ActivityCompat.requestPermissions(PersonActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

            //用于保存调用相机拍照后所生成的文件
            String filename = getRandomString(10);
            tempFile = new File(Environment.getExternalStorageDirectory().getPath(), filename+"head.jpg");
            //跳转到调用系统相机
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //判断版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
                intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(PersonActivity.this, "com.example.hello.fileproviser", tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                Log.e("相机", contentUri.toString());
            } else {    //否则使用Uri.fromFile(file)方法获取Uri
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
            }
            startActivityForResult(intent, CAMERA_REQUEST_CODE);*/
            dialog.dismiss();
            Toast.makeText(this,"开发太懒了，暂时无法使用",Toast.LENGTH_SHORT).show();
        });
        dialog.setView(view);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:   //调用相机后返回
                //用相机返回的照片去调用剪裁也需要对Uri进行处理
                Uri uri = intent.getData();
                String path = getFilePathForN(this, uri);
                headphoto.setImageURI(uri);
                break;
            case ALBUM_REQUEST_CODE:    //调用相册后返回
                if (resultCode == RESULT_OK) {
                    uri = intent.getData();
                    path = getFilePathForN(this, uri);
                    if (i == 0) {
                        beijingpath = path;
                        beijingban.setImageURI(uri);
                    } else {
                        headphoto.setImageURI(uri);
                        headphotopath = path;
                    }

                    i = -1;
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    private static String getFilePathForN(Context context, Uri uri) {
        try {
            Cursor returnCursor = context.getContentResolver().query(uri, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            String name = (returnCursor.getString(nameIndex));
            File file = new File(context.getFilesDir(), name);
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1024 * 1024;
            int bytesAvailable = inputStream.available();
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            returnCursor.close();
            inputStream.close();
            outputStream.close();
            return file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}






