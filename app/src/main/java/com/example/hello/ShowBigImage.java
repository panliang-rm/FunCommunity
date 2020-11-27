package com.example.hello;

import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

public class ShowBigImage {

    /**
     * 实现点击图片展示大图*/
    public static void showBigImage(String photo, Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View imgEntryView = inflater.inflate(R.layout.dialog_photo, null);
        // 加载自定义的布局文件
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        ImageView img = imgEntryView.findViewById(R.id.large_image);
        Glide.with(context).load(Uri.fromFile(new File(photo))).into(img);
        dialog.getWindow().setDimAmount(1);
        dialog.setView(imgEntryView); // 自定义dialog
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();

        // 点击大图关闭dialog
        imgEntryView.setOnClickListener(paramView -> dialog.cancel());

    }
}
