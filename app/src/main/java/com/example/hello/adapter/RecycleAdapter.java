
package com.example.hello.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hello.R;
import com.example.hello.ShowBigImage;
import com.example.hello.ShowTime;
import com.example.hello.data.CommentsBean;
import com.example.hello.data.DBOpenHelper;
import com.example.hello.data.Like;
import com.example.hello.data.Title;
import com.example.hello.data.UserBean;
import com.example.hello.view.CommentsView;
import com.example.hello.view.InputTextMsgDialog;
import com.example.hello.view.MultiImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 动态的适配器*/
public class RecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final ArrayList<Title> titleArrayList;
    private final LayoutInflater mInflater;
    private DBOpenHelper dbOpenHelper;
    private  boolean  isIconChange = true;


    //创建构造函数
    public RecycleAdapter(Context context, ArrayList<Title> titleArrayList) {
        //将传递过来的数据，赋值给本地变量
        this.context = context;//上下文
        this.titleArrayList = titleArrayList;//实体类数据ArrayList
        mInflater = LayoutInflater.from(context);
    }

    /**
     * 通过position获取item的类型
     * 有3种*/
    @Override
    public  int getItemViewType(int position) {
        Title title =titleArrayList.get(position);
        if( null != title .getPhoto() && title.getTitle()!= null) {
            return 1;
        } else if (title.getTitle()== null && title.getPhoto() != null){
            return  3;
        } else if(title .getPhoto()== null && title.getTitle() !=null) {
            return  2;
        } else
            return 0;
    }



    /**
     * 根据不同的类型创建不同的viewholder
     * 用于绑定数据*/
    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == 2) {
            return new RecyclerViewHolder2(mInflater.inflate(R.layout.view_textview, null));

        } else if (viewType == 3) {
            return new RecyclerViewHolder3(mInflater.inflate(R.layout.listview_photo, null));
        } else if (viewType == 1) {
            return new RecyclerViewHolder1(mInflater.inflate(R.layout.listview, null));
        }
       return null;
    }

    @Override
    public long getItemId(int position)
    { return position;}
    /**
     * */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //根据点击位置绑定数据
        Title data = titleArrayList.get(position);
        dbOpenHelper = new DBOpenHelper(context);
        /*
        * 长按item的点击事件*/
        if (onItemClickListener!= null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.OnItemClick(holder.itemView, position);
                    }
                    return false;
                }
            });
        }

        //图片+文字
        if (holder instanceof RecyclerViewHolder1) {
            System.out.println("调用1");
            ((RecyclerViewHolder1)holder).title.setText(data.getTitle());//获取实体类中的title字段并设置
            ((RecyclerViewHolder1)holder).name.setText(data.getUser_id());//设置用户名


            //加载头像
            if(data.getHeadphoto() != null) {
                Glide.with(context).load(Uri.fromFile(new File(data.getHeadphoto()))).into(((RecyclerViewHolder1)holder).headphoto);
            } else
                Glide.with(context).load(R.drawable.origin).into(((RecyclerViewHolder1)holder).headphoto);//如果未设置则显示默认头像
            //点击头像显示个人界面
            ((RecyclerViewHolder1)holder).headphoto.setOnClickListener(v -> clickHeadPhotoShowDetails(data));

            //多图显示
            Type type3 = new TypeToken<List<String>>(){}.getType();
            if (data.getPhoto() != null) {
                /*数据库取出变量photo,因为存储的时候是通过json: String photo = gson.toJson(photolist);
                * 转化为String类型
                * 取出时再使用gson转为 List<String>*/
                List<String> photolist = new Gson().fromJson(data.getPhoto(),type3);
                ((RecyclerViewHolder1)holder).photo.setList(photolist);//此Photo为MultiImageView类型，专门用来显示多张图片，已封装
            }


            /**
             * 点赞处理
             * 大概就是先查询数据库中是否存在
             * 若存在，则取出并转为like类型的集合*/
            //点击点赞图片的处理
            String like = data.getLike();
            List<Like> likeList;
            if (like != null)
                likeList = new Gson().fromJson(like,new TypeToken<List<Like>>(){}.getType());
            else
                likeList  = new ArrayList<>();
            if (likeList.size() != 0) {
                for (int i = 0; i<likeList.size(); i++) {
                    if (likeList.get(i).getUser().equals(dbOpenHelper.getUser())) {

                        ((RecyclerViewHolder1) holder).btLike.setImageResource(R.drawable.dianzan1);
                        isIconChange = false;
                    } else
                        ((RecyclerViewHolder1) holder).btLike.setImageResource(R.drawable.dianzan);
                }

            }

            ((RecyclerViewHolder1)holder).btLike.setOnClickListener(v -> {
                Like islike = new Like();
                Gson gson = new Gson();

                if (isIconChange) {
                    ((RecyclerViewHolder1)holder).btLike.setImageResource(R.drawable.dianzan1);
                    isIconChange = false;
                    islike.setUser(dbOpenHelper.getUser());
                    islike.setId(data.getId());

                    likeList.add(islike);
                    String string = gson.toJson(likeList);
                    dbOpenHelper.updateLike(string,data.getId());
                } else
                {
                    ((RecyclerViewHolder1)holder).btLike.setImageResource(R.drawable.dianzan);
                    isIconChange = true;
                    for (int i = 0; i < likeList.size(); i++) {
                        if (likeList.get(i).getUser().equals(dbOpenHelper.getUser())) {
                            likeList.remove(i);
                            String string = gson.toJson(likeList);
                            dbOpenHelper.updateLike(string,data.getId());
                        }
                    }
                }

            });

            /*
            * 评论处理
            * 创建commentview为加载数据类型为List<CommentsBean>的评论
            * 每一条评论的类型为CommentsBean
            * 因为数据库不接受集合类型
            * 需要json的转化
            *
            * Recycleview初始话时
            * 从数据库取出数据
            * 如果为空则创建新的list*/
            Type type1 = new TypeToken<List<CommentsBean>>(){}.getType();
            List<CommentsBean> list = new Gson().fromJson(data.getComments(),type1);
            if (list == null) {
                list = new ArrayList<>();
            }
            List<CommentsBean> finalList = list;
            //如果不为空可以加载数据
            if (finalList != null) {
                ((RecyclerViewHolder1) holder).commentsView.setList(finalList); //设置数据
                ((RecyclerViewHolder1) holder).commentsView.setVisibility(View.VISIBLE); //设置排列方式为垂直
                ((RecyclerViewHolder1) holder).commentsView.notifyDataSetChanged(); //刷新评论数据
                //点击事件弹出对话框InputTextMsgDialog
                ((RecyclerViewHolder1) holder).commentsView.setOnItemClickListener((position17, bean) -> {
                    InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(context, R.style.dialog_center);
                    inputTextMsgDialog.show();
                    //对话框的点击发送事件
                    inputTextMsgDialog.setmOnTextSendListener(msg -> {
                        //点击发送按钮后，回调此方法，msg为输入的值
                        CommentsBean commentsBean = new CommentsBean(); //创建一条新的评论
                        UserBean userBean1 = new UserBean();
                        UserBean userBean2 = new UserBean();
                        commentsBean.setContent(msg);
                        commentsBean.setCommentsUser(userBean1);
                        userBean1.setUserName(dbOpenHelper.getUser());
                        userBean2.setUserName(data.getUser_id());
                        commentsBean.setReplyUser(bean.getCommentsUser());
                        commentsBean.setId(data.getId());
                        finalList.add(commentsBean);

                        ((RecyclerViewHolder1) holder).commentsView.setList(finalList);
                        ((RecyclerViewHolder1) holder).commentsView.setVisibility(View.VISIBLE);
                        ((RecyclerViewHolder1) holder).commentsView.notifyDataSetChanged();
                        dbOpenHelper.addcomments(new Gson().toJson(finalList),data.getId());
                    });
                });
                ((RecyclerViewHolder1) holder).commentsView.setLongClickListener((position16, bean) -> {
                    //添加取消
                    AlertDialog alertDialog = new AlertDialog.Builder(context)
                            .setTitle("删除评论")
                            .setNegativeButton("取消", (dialogInterface, i) -> {
                            })
                            .setPositiveButton("确定", (dialog, which) -> {
                                if (dbOpenHelper.getUser().equals(finalList.get(position16).getCommentsUser().getUserName())) {
                                    finalList.remove(position16);
                                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                    ((RecyclerViewHolder1) holder).commentsView.setList(finalList);
                                    ((RecyclerViewHolder1) holder).commentsView.notifyDataSetChanged();
                                    dbOpenHelper.addcomments(new Gson().toJson(finalList), data.getId());
                                } else {
                                    Toast.makeText(context, "无法删除", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                });
            }


            ((RecyclerViewHolder1) holder).istalk.setOnClickListener(v -> {
                InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(context, R.style.dialog_center);
                inputTextMsgDialog.show();
                inputTextMsgDialog.setmOnTextSendListener(msg -> {
                    //点击发送按钮后，回调此方法，msg为输入的值
                    CommentsBean commentsBean = new CommentsBean();
                    UserBean userBean1 = new UserBean();
                    commentsBean.setContent(msg);
                    commentsBean.setCommentsUser(userBean1);
                    userBean1.setUserName(dbOpenHelper.getUser());
                    commentsBean.setId(data.getId());
                    finalList.add(commentsBean);

                    ((RecyclerViewHolder1) holder).commentsView.setList(finalList);
                    ((RecyclerViewHolder1) holder).commentsView.setVisibility(View.VISIBLE);
                    ((RecyclerViewHolder1) holder).commentsView.notifyDataSetChanged();
                    dbOpenHelper.addcomments(new Gson().toJson(finalList),data.getId());

                });
            });

            //仿造微信显示时间：刚刚
            Date date = new Gson().fromJson(data.getTime(), new TypeToken<Date>(){}.getType());
            ((RecyclerViewHolder1) holder).istime.setText(ShowTime.getTimeStringAutoShort2(date,false));

            //点击分享弹出
            ((RecyclerViewHolder1) holder).share.setOnClickListener(v -> Toast.makeText(context,"你点击了分享，我们已经收到o(*￣▽￣*)ブ",Toast.LENGTH_SHORT).show());
        } else if(holder instanceof  RecyclerViewHolder2) {


            ((RecyclerViewHolder2) holder).share.setOnClickListener(v -> Toast.makeText(context,"你点击了分享，我们已经收到o(*￣▽￣*)ブ",Toast.LENGTH_SHORT).show());
          //头像
            if(data.getHeadphoto() != null) {
                Glide.with(context).load(Uri.fromFile(new File(data.getHeadphoto()))).into(((RecyclerViewHolder2)holder).headphoto);
            } else
                Glide.with(context).load(R.drawable.origin).into(((RecyclerViewHolder2)holder).headphoto);
            ((RecyclerViewHolder2)holder).headphoto.setOnClickListener(v -> clickHeadPhotoShowDetails(data));

            ((RecyclerViewHolder2) holder).title.setText(data.getTitle());
            ((RecyclerViewHolder2) holder).name.setText(data.getUser_id());


            Type type1 = new TypeToken<List<CommentsBean>>(){}.getType();
            List<CommentsBean> list = new Gson().fromJson(data.getComments(),type1);
            if (list == null) {
                list = new ArrayList<>();
            }
            List<CommentsBean> finalList = list;
            if (finalList != null) {
                ((RecyclerViewHolder2) holder).commentsView.setList(finalList);
                ((RecyclerViewHolder2) holder).commentsView.setVisibility(View.VISIBLE);
                ((RecyclerViewHolder2) holder).commentsView.notifyDataSetChanged();
                ((RecyclerViewHolder2) holder).commentsView.setOnItemClickListener((position15, bean) -> {
                    InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(context, R.style.dialog_center);
                    inputTextMsgDialog.show();
                    inputTextMsgDialog.setmOnTextSendListener(msg -> {
                        //点击发送按钮后，回调此方法，msg为输入的值
                        CommentsBean commentsBean = new CommentsBean();
                        UserBean userBean1 = new UserBean();
                        UserBean userBean2 = new UserBean();
                        commentsBean.setContent(msg);
                        commentsBean.setCommentsUser(userBean1);
                        userBean1.setUserName(dbOpenHelper.getUser());
                        userBean2.setUserName(data.getUser_id());
                        commentsBean.setReplyUser(bean.getCommentsUser());
                        commentsBean.setId(data.getId());
                        finalList.add(commentsBean);

                        ((RecyclerViewHolder2) holder).commentsView.setList(finalList);
                        ((RecyclerViewHolder2) holder).commentsView.setVisibility(View.VISIBLE);
                        ((RecyclerViewHolder2) holder).commentsView.notifyDataSetChanged();
                        dbOpenHelper.addcomments(new Gson().toJson(finalList),data.getId());
                    });
                });

                ((RecyclerViewHolder2) holder).commentsView.setLongClickListener((position14, bean) -> {
                    //添加取消
                    AlertDialog alertDialog = new AlertDialog.Builder(context)
                            .setTitle("删除评论")
                            .setNegativeButton("取消", (dialogInterface, i) -> {
                            })
                            .setPositiveButton("确定", (dialog, which) -> {
                                if (dbOpenHelper.getUser().equals(finalList.get(position14).getCommentsUser().getUserName())) {
                                    finalList.remove(position14);
                                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                    ((RecyclerViewHolder2) holder).commentsView.setList(finalList);
                                    ((RecyclerViewHolder2) holder).commentsView.notifyDataSetChanged();
                                    dbOpenHelper.addcomments(new Gson().toJson(finalList), data.getId());
                                } else {
                                    Toast.makeText(context, "无法删除", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                });
            }


            ((RecyclerViewHolder2) holder).istalk.setOnClickListener(v -> {
                InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(context, R.style.dialog_center);
                inputTextMsgDialog.show();
                inputTextMsgDialog.setmOnTextSendListener(msg -> {
                    //点击发送按钮后，回调此方法，msg为输入的值
                    CommentsBean commentsBean = new CommentsBean();
                    UserBean userBean1 = new UserBean();
                    commentsBean.setContent(msg);
                    commentsBean.setCommentsUser(userBean1);
                    userBean1.setUserName(dbOpenHelper.getUser());
                    commentsBean.setId(data.getId());
                    finalList.add(commentsBean);

                    ((RecyclerViewHolder2) holder).commentsView.setList(finalList);
                    ((RecyclerViewHolder2) holder).commentsView.setVisibility(View.VISIBLE);
                    ((RecyclerViewHolder2) holder).commentsView.notifyDataSetChanged();
                    dbOpenHelper.addcomments(new Gson().toJson(finalList),data.getId());

                });
            });

            String like = data.getLike();
            List<Like> likeList;
            if (like != null)
                likeList = new Gson().fromJson(like,new TypeToken<List<Like>>(){}.getType());
            else
                likeList  = new ArrayList<>();
            if (likeList.size() != 0) {
                for (int i = 0; i<likeList.size(); i++) {
                      if (likeList.get(i).getUser().equals(dbOpenHelper.getUser())) {

                          ((RecyclerViewHolder2) holder).btLike.setImageResource(R.drawable.dianzan1);
                          isIconChange = false;
                    } else
                          ((RecyclerViewHolder2) holder).btLike.setImageResource(R.drawable.dianzan);
                }

            }

            ((RecyclerViewHolder2)holder).btLike.setOnClickListener(v -> {
                Like islike = new Like();
                Gson gson = new Gson();

                if (isIconChange) {
                    ((RecyclerViewHolder2)holder).btLike.setImageResource(R.drawable.dianzan1);
                    isIconChange = false;
                    islike.setUser(dbOpenHelper.getUser());
                    islike.setId(data.getId());

                    likeList.add(islike);
                    String string = gson.toJson(likeList);
                    dbOpenHelper.updateLike(string,data.getId());
                } else
                {
                    ((RecyclerViewHolder2)holder).btLike.setImageResource(R.drawable.dianzan);
                    isIconChange = true;
                    for (int i = 0; i < likeList.size(); i++) {
                        if (likeList.get(i).getUser().equals(dbOpenHelper.getUser())) {
                            likeList.remove(i);
                            String string = gson.toJson(likeList);
                            dbOpenHelper.updateLike(string,data.getId());
                        }
                    }
                }

            });
            Date date = new Gson().fromJson(data.getTime(), new TypeToken<Date>(){}.getType());
            ((RecyclerViewHolder2) holder).istime.setText(ShowTime.getTimeStringAutoShort2(date,false));


        }  else if(holder instanceof  RecyclerViewHolder3){

            Date date = new Gson().fromJson(data.getTime(), new TypeToken<Date>(){}.getType());
            ((RecyclerViewHolder3) holder).istime.setText(ShowTime.getTimeStringAutoShort2(date,false));

            ((RecyclerViewHolder3) holder).name.setText(data.getUser_id());
            //加载头像
            //加载图片
            if(data.getHeadphoto() != null) {
                Glide.with(context).load(Uri.fromFile(new File(data.getHeadphoto()))).into(((RecyclerViewHolder3)holder).headphoto);
            } else
                Glide.with(context).load(R.drawable.origin).into(((RecyclerViewHolder3)holder).headphoto);
            ((RecyclerViewHolder3)holder).headphoto.setOnClickListener(v -> clickHeadPhotoShowDetails(data));
            //Recycleview显示多张图片

            Type type3 = new TypeToken<List<String>>(){}.getType();
            List<String> photolist = new Gson().fromJson(data.getPhoto(),type3);
            ((RecyclerViewHolder3)holder).photo.setList(photolist);

            String like = data.getLike();
            List<Like> likeList;
            if (like != null)
                likeList = new Gson().fromJson(like,new TypeToken<List<Like>>(){}.getType());
            else
                likeList  = new ArrayList<>();
            if (likeList.size() != 0) {
                for (int i = 0; i<likeList.size(); i++) {
                    if (likeList.get(i).getUser().equals(dbOpenHelper.getUser())) {

                        ((RecyclerViewHolder3) holder).btLike.setImageResource(R.drawable.dianzan1);
                        isIconChange = false;
                    } else
                        ((RecyclerViewHolder3) holder).btLike.setImageResource(R.drawable.dianzan);
                }

            }

            ((RecyclerViewHolder3)holder).btLike.setOnClickListener(v -> {
                Like islike = new Like();
                Gson gson = new Gson();

                if (isIconChange) {
                    ((RecyclerViewHolder3)holder).btLike.setImageResource(R.drawable.dianzan1);
                    isIconChange = false;
                    islike.setUser(dbOpenHelper.getUser());
                    islike.setId(data.getId());

                    likeList.add(islike);
                    String string = gson.toJson(likeList);
                    dbOpenHelper.updateLike(string,data.getId());
                } else
                {
                    ((RecyclerViewHolder3)holder).btLike.setImageResource(R.drawable.dianzan);
                    isIconChange = true;
                    for (int i = 0; i < likeList.size(); i++) {
                        if (likeList.get(i).getUser().equals(dbOpenHelper.getUser())) {
                            likeList.remove(i);
                            String string = gson.toJson(likeList);
                            dbOpenHelper.updateLike(string,data.getId());
                        }
                    }
                }

            });
            Type type1 = new TypeToken<List<CommentsBean>>(){}.getType();
            List<CommentsBean> list = new Gson().fromJson(data.getComments(),type1);
            if (list == null) {
                list = new ArrayList<>();
            }
            List<CommentsBean> finalList = list;
            if (finalList != null) {
                ((RecyclerViewHolder3) holder).commentsView.setList(finalList);
                ((RecyclerViewHolder3) holder).commentsView.setVisibility(View.VISIBLE);
                ((RecyclerViewHolder3) holder).commentsView.notifyDataSetChanged();
                ((RecyclerViewHolder3) holder).commentsView.setOnItemClickListener((position12, bean) -> {
                    InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(context, R.style.dialog_center);
                    inputTextMsgDialog.show();
                    inputTextMsgDialog.setmOnTextSendListener(msg -> {
                        //点击发送按钮后，回调此方法，msg为输入的值
                        CommentsBean commentsBean = new CommentsBean();
                        UserBean userBean1 = new UserBean();
                        UserBean userBean2 = new UserBean();
                        commentsBean.setContent(msg);
                        commentsBean.setCommentsUser(userBean1);
                        userBean1.setUserName(dbOpenHelper.getUser());
                        userBean2.setUserName(data.getUser_id());
                        commentsBean.setReplyUser(bean.getCommentsUser());
                        commentsBean.setId(data.getId());
                        finalList.add(commentsBean);

                        ((RecyclerViewHolder3) holder).commentsView.setList(finalList);
                        ((RecyclerViewHolder3) holder).commentsView.setVisibility(View.VISIBLE);
                        ((RecyclerViewHolder3) holder).commentsView.notifyDataSetChanged();
                        dbOpenHelper.addcomments(new Gson().toJson(finalList),data.getId());
                    });
                });
                ((RecyclerViewHolder3) holder).commentsView.setLongClickListener((position1, bean) -> {
                    //添加取消
                    AlertDialog alertDialog = new AlertDialog.Builder(context)
                            .setTitle("删除评论")
                            .setNegativeButton("取消", (dialogInterface, i) -> {
                            })
                            .setPositiveButton("确定", (dialog, which) -> {
                                if (dbOpenHelper.getUser().equals(finalList.get(position1).getCommentsUser().getUserName())) {
                                    finalList.remove(position1);
                                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                    ((RecyclerViewHolder3) holder).commentsView.setList(finalList);
                                    ((RecyclerViewHolder3) holder).commentsView.notifyDataSetChanged();
                                    dbOpenHelper.addcomments(new Gson().toJson(finalList), data.getId());
                                } else {
                                    Toast.makeText(context, "无法删除", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .create();
                    alertDialog.setCancelable(false);
                    alertDialog.show();
                });
            }


            ((RecyclerViewHolder3) holder).istalk.setOnClickListener(v -> {
                InputTextMsgDialog inputTextMsgDialog = new InputTextMsgDialog(context, R.style.dialog_center);
                inputTextMsgDialog.show();
                inputTextMsgDialog.setmOnTextSendListener(msg -> {
                    CommentsBean commentsBean = new CommentsBean();
                    UserBean userBean1 = new UserBean();
                    commentsBean.setContent(msg);
                    commentsBean.setCommentsUser(userBean1);
                    userBean1.setUserName(dbOpenHelper.getUser());
                    commentsBean.setId(data.getId());
                    finalList.add(commentsBean);

                    ((RecyclerViewHolder3) holder).commentsView.setList(finalList);
                    ((RecyclerViewHolder3) holder).commentsView.setVisibility(View.VISIBLE);
                    ((RecyclerViewHolder3) holder).commentsView.notifyDataSetChanged();
                    dbOpenHelper.addcomments(new Gson().toJson(finalList),data.getId());

                });
            });
            ((RecyclerViewHolder3) holder).share.setOnClickListener(v -> Toast.makeText(context,"你点击了分享，我们已经收到o(*￣▽￣*)ブ",Toast.LENGTH_SHORT).show());
            }
        }


    private void clickHeadPhotoShowDetails(Title data) {
        Drawable drawable = Drawable.createFromPath(data.getHeadphoto());
        Drawable drawable1;
             drawable1 = Drawable.createFromPath(dbOpenHelper.getBeiJingForName(data.getUser_id()));

        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageDrawable(drawable1);
        imageView.setOnClickListener(v -> ShowBigImage.showBigImage(dbOpenHelper.getBeiJingForName(data.getUser_id()),context));
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(data.getUser_id())
                .setMessage("签名："+dbOpenHelper.getQingMingForName(data.getUser_id()))
                .setIcon(drawable)
                .setView(imageView)
                .create();
        alertDialog.show();
      
    }


    @Override
    public int getItemCount() {
        return titleArrayList.size();
    }



    //自定义viewhodler 图文
    private static class  RecyclerViewHolder1 extends RecyclerView.ViewHolder {
        private final MultiImageView photo;
        private final TextView title;
        private final TextView name;
        public CircleImageView headphoto;
        private final ImageView btLike;
        private final CommentsView commentsView;
        private final ImageView istalk;
        private final TextView istime;
        private final ImageView share;
        private   RecyclerViewHolder1(View view) {
            super(view);
            title =  view.findViewById(R.id.title);
            name = view.findViewById(R.id.username);
            photo = view.findViewById(R.id.photo);
            headphoto = view.findViewById(R.id.headphoto);
            istalk = view.findViewById(R.id.istalk);
            commentsView = view.findViewById(R.id.commentView);
            btLike = view.findViewById(R.id.islike);
            istime = view.findViewById(R.id.show_time);
            share = view.findViewById(R.id.share_photo_title);
        }
    }

    //文字
    private static class  RecyclerViewHolder2 extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView name;
        private final CircleImageView headphoto;
        private final ImageView istalk;
        private final CommentsView commentsView;
        private final ImageView btLike;
        private final TextView istime;
        private final ImageView share;
        private   RecyclerViewHolder2(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            name = view.findViewById(R.id.username);
            headphoto = view.findViewById(R.id.headphoto);
            istalk = view.findViewById(R.id.istalk_2);
            commentsView = view.findViewById(R.id.commentView);
            btLike = view.findViewById(R.id.islike_2);
            istime = view.findViewById(R.id.show_time);
            share = view.findViewById(R.id.share_title);
        }
    }

    //图片
    static class  RecyclerViewHolder3 extends RecyclerView.ViewHolder {
        private  MultiImageView photo;
        private TextView name;
        public CircleImageView headphoto;
        private ImageView btLike;
        private CommentsView commentsView;
        private ImageView istalk;
        private TextView istime;
        private ImageView share;
        private   RecyclerViewHolder3(View view) {
            super(view);
            photo = view.findViewById(R.id.photo);
            name = view.findViewById(R.id.username);
            headphoto = view.findViewById(R.id.headphoto);
            commentsView = view.findViewById(R.id.commentView);
            btLike = view.findViewById(R.id.islike_3);
            istalk = view.findViewById(R.id.istalk_3);
            istime = view.findViewById(R.id.show_time);
            share = view.findViewById(R.id.share_photo);
        }
    }


    /**
     * 设置item的监听事件的接口
     */
    public interface OnItemClickListener {

        void OnItemClick(View view, int i);
    }

    //需要外部访问，所以需要设置set方法，方便调用
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    }

