package com.example.hello.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.hello.R;
import com.example.hello.fragment.AFragment;
import com.example.hello.fragment.BFragment;
import com.example.hello.fragment.CFragment;
import com.example.hello.fragment.DFragment;
import com.next.easynavigation.view.EasyNavigationBar;

import java.util.ArrayList;
import java.util.List;


/**
 * 主界面
 * 实现navigation底部导航栏*/
public class MainActivity extends AppCompatActivity {

    //底部四个可点击栏
    private final String[] tabText = {"首页", "动态", "极光", "我的"};
    //未选中
    private final int[] normalIcon = {R.mipmap.index, R.mipmap.find, R.mipmap.message, R.mipmap.me};
    //选中时icon
    private final int[] selectIcon = {R.mipmap.index1, R.mipmap.find1,  R.mipmap.message1, R.mipmap.me1};

    //依附于MainActivity的四个framgent,分别显示四个界面
    private final List<Fragment> fragments = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AFragment aFragment = new AFragment();
        BFragment bFragment = new BFragment();
        CFragment cFragment = new CFragment();
        DFragment dFragment = new DFragment();

        //底部导航栏实例
        EasyNavigationBar navigationBar = findViewById(R.id.navigationBar);

        fragments.add(aFragment);
        fragments.add(bFragment);
        fragments.add(cFragment);
        fragments.add(dFragment);

        //初始化底部导航栏，设置属性
        navigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)

                .fragmentManager(getSupportFragmentManager())
                .setOnTabClickListener(new EasyNavigationBar.OnTabClickListener() {
                    @Override
                    public boolean onTabSelectEvent(View view, int position) {
                        return false;
                    }
                    @Override
                    public boolean onTabReSelectEvent(View view, int position) {
                        return false;
                    }
                })
                .smoothScroll(true)
                .canScroll(true)
                .mode(EasyNavigationBar.NavigationMode.MODE_ADD)
                .centerLayoutRule(EasyNavigationBar.RULE_BOTTOM)
                .centerImageRes(R.mipmap.add_image)
                .centerTextStr("发布")
                .centerIconSize(36)
                //点击中间的加号按钮，跳转到发布动态界面
                .setOnCenterTabClickListener(view -> {
                    Intent intent = new Intent(MainActivity.this,AddActivity.class);
                    startActivity(intent);
                    return false;
                })

                .build();


        }

    //当在主界面按下返回键，程序后台运行
    @Override
    public void onBackPressed(){
       moveTaskToBack(true);

    }

}



