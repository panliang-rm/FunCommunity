package com.example.hello.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hello.R;
import com.example.hello.adapter.NineGridAdapter;
import com.example.hello.data.DBOpenHelper;
import com.giftedcat.easylib.selector.MultiImageSelector;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 发布动态界面*/
public class AddActivity extends AppCompatActivity {

    ImageView imageView;
    private static final int REQUEST_IMAGE = 2;
    private int maxNum = 9;

    Unbinder unbinder;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_images)
    RecyclerView rvImages;

    NineGridAdapter adapter;

    List<String> mSelect;
    List<String> photo;

    DBOpenHelper dbOpenHelper;

    TextView textView;
    EditText editText;

    public Context context;
    public Activity instans;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        context = this;
        instans = this;

        unbinder = ButterKnife.bind(this);

        mSelect = new ArrayList<>();

        initView();



        dbOpenHelper = new DBOpenHelper(this);

        initonClick();


    }

    //点击完成后，保存不同的动态到数据库
    private void initonClick() {
        //返回主界面
        imageView.setOnClickListener(v -> finish());

        textView.setOnClickListener(v -> {
            Intent intent = new Intent(AddActivity.this,MainActivity.class);
            String text = editText.getText().toString().trim();
            String username = dbOpenHelper.getUser();
            String headphoto = dbOpenHelper.getHeadPhoto();
            Gson gson = new Gson();
            String time = gson.toJson(new Date());

            if(photo == null && text.length() == 0) {
                Toast.makeText(context,"内容为空，请输入图片/文字再发表",Toast.LENGTH_SHORT).show();
            } else if (photo == null) {
                dbOpenHelper.insertTitle(text,username,headphoto,time);
                dbOpenHelper.updatatuwenlogin(username);
                dbOpenHelper.close();
                startActivity(intent);
            } else if( text.length() == 0){
                if (photo.size()!= 9)
                    photo.remove(photo.size()-1);
                String string = gson.toJson(photo);
                dbOpenHelper.insertPhoto(username,string,headphoto,time);
                dbOpenHelper.updatatuwenlogin(username);
                dbOpenHelper.close();
                startActivity(intent);

            }  else {
                if (photo.size()!= 9)
                    photo.remove(photo.size()-1);
                String string = gson.toJson(photo);
                dbOpenHelper.insertTwo(text,username,string,headphoto,time);
                dbOpenHelper.updatatuwenlogin(username);
                dbOpenHelper.close();
                startActivity(intent);
            }

        });
    }


    private void initView() {
        imageView = findViewById(R.id.back);
        textView = findViewById(R.id.forward);
        editText = findViewById(R.id.dongtai);
        rvImages.setLayoutManager(new GridLayoutManager(this, 3));
        adapter = new NineGridAdapter(AddActivity.this, mSelect, rvImages);
        adapter.setMaxSize(maxNum);
        rvImages.setAdapter(adapter);
        adapter.setOnAddPicturesListener(this::pickImage);
    }

    /**
     * 选择添加图片
     */
    private void pickImage() {
        MultiImageSelector selector = MultiImageSelector.create(context);
        selector.showCamera(true);
        selector.count(maxNum);
        selector.multi();
        selector.origin(mSelect);
        selector.start(instans, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                 List<String> select = data.getStringArrayListExtra(MultiImageSelector.EXTRA_RESULT);
                 photo = select;
                mSelect.clear();
                mSelect.addAll(select);
                adapter.notifyDataSetChanged();
                for (int i = 0; i < select.size(); i++) {
                    Log.e("string",select.get(i));
                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unbinder.unbind();
    }
}
