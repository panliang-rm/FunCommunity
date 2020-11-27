package com.example.hello.fragment;

import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.example.hello.Loader.GlideImageLoader;
import com.example.hello.R;
import com.example.hello.adapter.RecycleAdapter;
import com.example.hello.adapter.SpacesItemDecoration;
import com.example.hello.data.DBOpenHelper;
import com.example.hello.data.Title;
import com.example.hello.view.MultiImageView;
import com.youth.banner.Banner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class AFragment extends Fragment {



    private View view;
    private ArrayList<Title> titleArrayList = new ArrayList<Title>();
    private RecycleAdapter mRecycleAdapter;
    private RecyclerView recyclerView;
    private DBOpenHelper dbOpenHelper;
    private CircleImageView circleImageView;
    private SwipeRefreshLayout swipeRefresh;// 刷新
    private Banner banner;
    private MultiImageView multiImageView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_a, container, false);

        dbOpenHelper = new DBOpenHelper(getContext());
        initView();
        initData();
        //进入Aframgent自动刷新
        autoRefresh();
        //下拉刷新
        swiprRefesh();
        //轮播图
        arousel();


        return view;
    }


    private void autoRefresh() {
        //首次进入自动刷新
        swipeRefresh.post(() -> swipeRefresh.setRefreshing(true));
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //要执行的操作
                swipeRefresh.post(() -> {
                    swipeRefresh.setRefreshing(false);
                    refresh();
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 2000);//2秒后执行TimeTask的run方法
    }

    private void arousel() {
        //轮播图
        banner = (Banner)view.findViewById(R.id.banner);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        List<String> images = new ArrayList<>();
        images.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2638811744,3189432810&fm=26&gp=0.jpg");
        images.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3318417841,1813671307&fm=26&gp=0.jpg");
        images.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2425387389,2500015817&fm=26&gp=0.jpg");
        images.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1982109569,4202656572&fm=26&gp=0.jpg");
        images.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1174877039,3996857209&fm=26&gp=0.jpg");
        banner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
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
                    Toast.makeText(getContext(),"刷新成功",Toast.LENGTH_SHORT).show();
                    swipeRefresh.setRefreshing(false);//隐藏刷新进度条
                });
            }).start();

        });
    }







    /**
     * 载入数据
     */
    private void initData() {
        ArrayList<Title> data = dbOpenHelper.getTitleData();
        int min = 0;
        int max = data.size();
        Random random = new Random();
        int num ,temp=-1;
        if (data.size() == 0) {

        } else {
            for (int i = 0; i <= data.size() /2 ; i++) {
                num = random.nextInt(max) % (max - min + 1) + min;
                while (num != temp) {
                    temp = num;
                    Title title = data.get(num);
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
    }



    /**
     * TODO 对recycleview进行配置
     */
    private void initView() {
        multiImageView = view.findViewById(R.id.photo);
        swipeRefresh =  view.findViewById(R.id.swipe_refresh);
        recyclerView = view.findViewById(R.id.title_recycleview1);
        circleImageView = view.findViewById(R.id.headphoto);
        if ( null!= dbOpenHelper.getHeadPhoto() ) {
            Glide.with(view).load(Uri.fromFile(new File(dbOpenHelper.getHeadPhoto()))).into(circleImageView);
        } else
            Glide.with(view).load((R.drawable.origin)).into(circleImageView);
        mRecycleAdapter = new RecycleAdapter(getContext(), titleArrayList);
        //给RecyclerView设置adapter
        mRecycleAdapter.setHasStableIds(true);
        recyclerView.setAdapter(mRecycleAdapter);
        mRecycleAdapter.setOnItemClickListener((view, i) -> {
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

        });
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

    private void refresh() {
        if ( null!= dbOpenHelper.getHeadPhoto() )
            Glide.with(view).load(Uri.fromFile(new File(dbOpenHelper.getHeadPhoto()))).into(circleImageView);

            titleArrayList.clear();
            initData();
            mRecycleAdapter.notifyDataSetChanged();

    }


}