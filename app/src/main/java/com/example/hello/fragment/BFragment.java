package com.example.hello.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.hello.R;
import com.example.hello.adapter.RecycleAdapter;
import com.example.hello.adapter.SpacesItemDecoration;
import com.example.hello.data.DBOpenHelper;
import com.example.hello.data.Title;
import com.example.hello.view.MultiImageView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class BFragment extends Fragment {



    private View view;
    private ArrayList<Title> titleArrayList = new ArrayList<Title>();
    private RecycleAdapter mRecycleAdapter;
    private RecyclerView recyclerView;
    private DBOpenHelper dbOpenHelper;
    private CircleImageView circleImageView;
    private SwipeRefreshLayout swipeRefresh;// 刷新
    private MultiImageView multiImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_b, container, false);

        dbOpenHelper = new DBOpenHelper(getContext());
        initView();
        initData();

        autoRefresh();
        //下拉刷新
        swiprRefesh();


        return view;
    }

    private void autoRefresh() {
        //首次进入自动刷新
        swipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(true);
            }
        });
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //要执行的操作
                swipeRefresh.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                        refresh();
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 2000);//2秒后执行TimeTask的run方法
    }




    private void swiprRefesh() {

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
                    refresh();
                    Toast.makeText(getContext(),"没有更多了，请待会再刷吧",Toast.LENGTH_SHORT).show();
                    swipeRefresh.setRefreshing(false);//隐藏刷新进度条
                });
            }).start();

        });
    }







    /**
     * 载入数据
     */
    private void initData() {
        if (dbOpenHelper.getTitleData() != null) {
            ArrayList<Title> data = dbOpenHelper.getTitleData();
            for (int i = 0; i < data.size(); i++) {
                Title title = data.get(i);
                title.setId(title.getId());
                title.setUser_id(title.getUser_id());
                title.setTitle(title.getTitle());
                title.setPhoto(title.getPhoto());
                title.setId(title.getId());
                title.setHeadphoto(title.getHeadphoto());
                title.setComments(title.getComments());
                title.setLike(title.getLike());
                title.setTime(title.getTime());

                titleArrayList.add(title);
            }
        }
    }



    /**
     * TODO 对recycleview进行配置
     */
    private void initView() {
        multiImageView = view.findViewById(R.id.photo);
        swipeRefresh =  view.findViewById(R.id.swipe_refresh);
        recyclerView = view.findViewById(R.id.title_recycleview);
        mRecycleAdapter = new RecycleAdapter(getContext(), titleArrayList);
        //给RecyclerView设置adapter
        mRecycleAdapter.setHasStableIds(true);

        recyclerView.setAdapter(mRecycleAdapter);
        mRecycleAdapter.setOnItemClickListener(this::showPopupMenu);
        //设置layoutManager,可以设置显示效果，是线性布局、grid布局，还是瀑布流布局
        //参数是：上下文、列表方向（横向还是纵向）、是否倒叙
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);//列表再底部开始展示，反转后由上面开始展示
        linearLayoutManager.setReverseLayout(true);//列表翻转
        linearLayoutManager.isAutoMeasureEnabled();//自动获取item的高低
        //设置item的分割线
        int space = 16;
        recyclerView.addItemDecoration(new SpacesItemDecoration(space));

    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }


    public void refresh() {

        titleArrayList.clear();
        initData();
        mRecycleAdapter.notifyDataSetChanged();
    }

    private void showPopupMenu(View view, int i) {
        //添加取消
        AlertDialog alertDialog = new AlertDialog.Builder(view.getContext())
                .setTitle("删除")
                .setNegativeButton("取消", (dialogInterface, i1) -> {
                })
                .setPositiveButton("确定", (dialog, which) -> {
                    Title data = titleArrayList.get(i);
                    String tuwenUser = data.getUser_id();
                    int id1 = data.getId();
                    String name = dbOpenHelper.getUser();
                    if (tuwenUser.equals(name)) {
                        titleArrayList.remove(i);
                        mRecycleAdapter.notifyDataSetChanged();
                        mRecycleAdapter.notifyItemRemoved(i);
                        dbOpenHelper.delete(id1);
                        Toast.makeText(getContext(),"删除成功",Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getContext(),"无法删除",Toast.LENGTH_SHORT).show();
                })
                .create();
        alertDialog.setCancelable(false);
        alertDialog.show();
}


}