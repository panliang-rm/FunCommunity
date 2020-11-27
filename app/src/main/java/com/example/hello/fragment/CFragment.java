package com.example.hello.fragment;


import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.hello.R;

/**
 * 简单的一个webview，用于显示极光主页
 */
public class CFragment extends Fragment {

    private WebView webView;
    private SwipeRefreshLayout swipeRefresh;// 刷新

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_c, container, false);
        webView = root.findViewById(R.id.b_webview);
        swipeRefresh =  root.findViewById(R.id.swipe_refresh);
        swiprRefesh();
        WebSettings settings = webView.getSettings();
        settings.setDomStorageEnabled(true);


        settings.setSupportZoom(true);// 支持缩放(适配到当前屏幕)
        settings.setUseWideViewPort(true);      // 将图片调整到合适的大小
        //settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL); // 支持内容重新布局,一共有四种方式 默认的是NARROW_COLUMNS
        settings.setDefaultFontSize(12);   // 设置默认字体大小
        webView.loadUrl("http://auroralab.club/");

        //实现：WebView里的链接，都在自身打开，不调用系统浏览器
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

        });

        //实现：按手机回退键，如果浏览器有上一个网页，则返回上一个网页
        webView.setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack() ) {
                    webView.goBack();
                    return true;
                }
            }
            return false;
        });
        return root;
    }

    private void swiprRefesh() {
        webView.reload();
        swipeRefresh.setColorSchemeResources(R.color.holo_blue_bright);//设置刷新进度条颜色
        swipeRefresh.setOnRefreshListener(() -> {
            // 处理刷新逻辑
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(() -> {
                    webView.reload();
                    swipeRefresh.setRefreshing(false);//隐藏刷新进度条
                });
            }).start();

        });
    }
}