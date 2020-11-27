package com.example.hello.fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.hello.R;
import com.example.hello.ShowBigImage;
import com.example.hello.activity.PersonActivity;
import com.example.hello.activity.SetActivity;
import com.example.hello.data.DBOpenHelper;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 个人界面*/
public class DFragment extends Fragment {

    private TextView name;
    private CircleImageView circleImageView;
    private DBOpenHelper dbOpenHelper;
    private LinearLayout editting;
    private TextView qianming;
    private LinearLayout setting;
    private LinearLayout shichang;
    private LinearLayout redu;
    private LinearLayout jiangzhnag;
    private LinearLayout huiyusn;
    private LinearLayout showperson;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_d, null);

        dbOpenHelper = new DBOpenHelper(getContext());
        initview(view);
        initOnClick();
        String username = dbOpenHelper.getUser();
        String userqianming = dbOpenHelper.getQingMing();
        name.setText(username);
        qianming.setText(userqianming);
        if (dbOpenHelper.getHeadPhoto() != null) {
           Glide.with(view.getContext()).load(dbOpenHelper.getHeadPhoto()).into(circleImageView);
        }


        return view;
    }



    private void initOnClick() {


        editting.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PersonActivity.class);
            startActivity(intent);
        });

        setting.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SetActivity.class);
            startActivity(intent);
        });



        showperson.setOnClickListener(v -> {
            Drawable drawable = Drawable.createFromPath(dbOpenHelper.getHeadPhoto());
            Drawable drawable1 = Drawable.createFromPath(dbOpenHelper.getBeiJing());
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageDrawable(drawable1);
            imageView.setOnClickListener(v1 -> ShowBigImage.showBigImage(dbOpenHelper.getBeiJing(),getContext()));

            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                    .setTitle(dbOpenHelper.getUser())
                    .setMessage("签名："+dbOpenHelper.getQingMing())
                    .setIcon(drawable)
                    .setView(imageView)
                    .create();
            alertDialog.show();

        });
        shichang.setOnClickListener(v -> Toast.makeText(getActivity(), "你点击了时长", Toast.LENGTH_SHORT).show());

        redu.setOnClickListener(v -> Toast.makeText(getActivity(), "你点击了热度", Toast.LENGTH_SHORT).show());

        jiangzhnag.setOnClickListener(v ->  Toast.makeText(getActivity(), "你点击了我的奖章", Toast.LENGTH_SHORT).show());

        huiyusn.setOnClickListener(v ->  Toast.makeText(getActivity(), "你点击了我的会员", Toast.LENGTH_SHORT).show());
    }

    private void initview(View view) {
        showperson = view.findViewById(R.id.show_person);
        name = view.findViewById(R.id.name);
        editting = view.findViewById(R.id.editting);
        setting = view.findViewById(R.id.setting);
        circleImageView = view.findViewById(R.id.circleimage);
        qianming = view.findViewById(R.id.qianming);
        shichang = view.findViewById(R.id.shichang);
        redu = view.findViewById(R.id.redu);
        jiangzhnag = view.findViewById(R.id.jiangzhang);
        huiyusn = view.findViewById(R.id.huiyuan);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();

    }

    protected void refresh() {
        dbOpenHelper = new DBOpenHelper(getContext());
        String username = dbOpenHelper.getUser();
        String userqianming = dbOpenHelper.getQingMing();
        name.setText(username);
        qianming.setText(userqianming);
        String path = dbOpenHelper.getHeadPhoto();
        if (path != null) {
            Glide.with(getContext()).load(path).into(circleImageView);
        }
    }
}