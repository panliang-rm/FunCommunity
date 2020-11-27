package com.example.hello.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * 数据库创建，操作类*/
public class DBOpenHelper extends SQLiteOpenHelper {

    //声明一个AndroidSDK自带的数据库变量db
    private final SQLiteDatabase db;

    private static final String sql = "CREATE TABLE IF NOT EXISTS user(" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT," +
            "password TEXT," +
            "headphoto TEXT," +
            "qianming TEXT," +
            "sex INTEGER, " +
            "beijing TEXT," +
            "online Text)";


    private static final String tuwen = "CREATE TABLE tuwen (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "title TEXT," +
            "user_id TEXT," +
            "headphoto TEXT," +
            "photo TEXT," +
            "islike TEXT," +
            "comments TEXT ," +
            "time TEXT ," +
            "online Text)";







    /**
     * 写一个这个类的构造函数，参数为上下文context，所谓上下文就是这个类所在包的路径
     * 指明上下文，数据库名，工厂默认空值，版本号默认从1开始
     * super(context,"db_test",null,1);
     * 把数据库设置成可写入状态，除非内存已满，那时候会自动设置为只读模式
     * 不过，以现如今的内存容量，估计一辈子也见不到几次内存占满的状态
     * db = getReadableDatabase();
     *
     * @param context
     */
    public DBOpenHelper(Context context) {
        super(context, "test.db", null, 12);
        db = getWritableDatabase();
    }


    /**
     * 重写两个必须要重写的方法，因为class DBOpenHelper extends SQLiteOpenHelper
     * 而这两个方法是 abstract 类 SQLiteOpenHelper 中声明的 abstract 方法
     * 所以必须在子类 DBOpenHelper 中重写 abstract 方法
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);
        db.execSQL(tuwen);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 接下来写自定义的增删改查方法
     * 这些方法，写在这里归写在这里，以后不一定都用
     */
    public void add(String name, String password, String s) {
        db.execSQL("INSERT INTO user (name,password,online) VALUES(?,?,?)", new Object[]{name, password, s});

    }

    public void delete(int id1) {

       db.delete("tuwen","id = ?",new String[]{String.valueOf(id1)});
    }

    public void updatePassWord (String newpassword) {
        db.execSQL("UPDATE user SET password = ? WHERE online = 1",new String[]{newpassword});
    }
    public void updateLike (String like,int id) {
        db.execSQL("UPDATE tuwen SET islike = ? WHERE id = ?",new Object[]{like,id});

    }
    public void update(String name) {
        db.execSQL("UPDATE user SET online = 1 Where name = ?" ,new String[]{name});
    }

    public void updataZero(String name) {
            db.execSQL("UPDATE user SET online = 0 WHERE name = ?",new String[] {name} );
    }

    public void addQianMing(String text, String username) {
        db.execSQL("UPDATE user Set qianming = ? WHERE name = ?" , new String[]{text,username} );
    }
    public void updatatuwenzero() {
        db.execSQL("UPDATE tuwen SET online = 0");
    }

    public void updateBeiJing(String path) {
        db.execSQL("UPDATE user SET beijing = ? WHERE online = 1" , new Object[]{path});
    }
    public void updatatuwenlogin(String name) {
       // String update = new StringBuilder().append("UPDATE tuwen SET online = ").append("1").append(" WHERE user_id = ").append(name).toString();
       // db.execSQL(update);
        db.execSQL("UPDATE tuwen SET online = '1' WHERE user_id = ?", new Object[]{name} );
    }
    public void updataHeadPhoto(String headphoto) {
        db.execSQL("UPDATE user SET headphoto = ? WHERE online = 1" , new Object[]{headphoto});
    }

    public void updatatuwenHeadPhoto(String headphoto) {
        db.execSQL("UPDATE tuwen SET headphoto = ? WHERE online = 1", new Object[]{headphoto});
    }
    public void updataName(String name) {
        db.execSQL("UPDATE user SET name = ? WHERE online = 1", new Object[]{name});
    }

    public void updatatuwenName(String name) {
        db.execSQL("UPDATE tuwen SET user_id = ? WHERE online = 1", new Object[]{name});
    }
    public void insertTitle(String text, String user_id, String headphoto,String time) {
        db.execSQL("INSERT INTO tuwen(title,user_id,headphoto,time) VALUES(?,?,?,?)", new Object[]{text,user_id,headphoto,time});
    }

    public void insertPhoto( String user_id, String photo,String headphoto,String time) {
        db.execSQL("INSERT INTO tuwen(user_id,photo,headphoto,time) VALUES(?,?,?,?)", new Object[]{user_id,photo,headphoto,time});
    }

    public void insertTwo(String text,String user_id, String photo,String headphoto,String time) {
        db.execSQL("INSERT INTO tuwen(title,user_id,photo,headphoto,time) VALUES(?,?,?,?,?)", new Object[]{text,user_id,photo,headphoto,time});
    }
    public ArrayList<User> getAllData() {

        ArrayList<User> list = new ArrayList<User>();
        Cursor cursor = db.query("user", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            String online = cursor.getString((cursor.getColumnIndex("online")));
            list.add(new User(name ,password,online));
        }
        cursor.close();
        return list;
    }

    public User getUserDetail(String username) {
        User user = new User();
        Cursor cursor = db.query("user",null,"name = ?",new String[]{username},null,null,null,null);
        if(cursor!=null){
            while(cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String headphoto = cursor.getString(cursor.getColumnIndex("headphoto"));
                String qianming = cursor.getString(cursor.getColumnIndex("qianming"));
                user.setName(name);
                user.setHeadphoto(headphoto);
                user.setQianming(qianming);
            }
        }
        cursor.close();
        return user;
    }

    public ArrayList<Title> getTitleData() {
        ArrayList<Title> list = new ArrayList<>();
        Cursor cursor = db.query("tuwen", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String user_id = cursor.getString(cursor.getColumnIndex("user_id"));
            String photo = cursor.getString(cursor.getColumnIndex("photo"));
            String headphoto = cursor.getString(cursor.getColumnIndex("headphoto"));
            String comments = cursor.getString((cursor.getColumnIndex("comments")));
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String like = cursor.getString((cursor.getColumnIndex("islike")));
            String time = cursor.getString((cursor.getColumnIndex("time")));
            list.add(new Title(title,id,photo,user_id,headphoto,comments,like,time));
        }
        cursor.close();
        return list;
    }

    public ArrayList<String> getAllLike() {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.query("tuwen", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String like = cursor.getString(cursor.getColumnIndex("islike"));
            list.add(like);
        }
        cursor.close();
        return list;
    }

    public ArrayList<String> getPhotoList() {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.query("user", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String headphoto = cursor.getString(cursor.getColumnIndex("headphoto"));
           list.add(headphoto);
        }
        cursor.close();
        return list;
    }


    public String getUser() {
        String username = null;
        Cursor cursor = db.query("user", null, "online = ?", new String[]{"1"}, null, null, null, null);
        if (cursor.moveToFirst()) {
             username = cursor.getString(cursor.getColumnIndex("name"));
        }
        cursor.close();
        return username;
    }
    public String getQingMing() {
        String qianming = null;
        Cursor cursor = db.query("user", null, "online = ?", new String[]{"1"}, null, null, null, null);
        if (cursor.moveToFirst()) {
            qianming = cursor.getString(cursor.getColumnIndex("qianming"));
        }
        cursor.close();
        return qianming;
    }

    public String getQingMingForName(String name) {
        String qianming = null;
        Cursor cursor = db.query("user", null, "name = ?", new String[]{name}, null, null, null, null);
        if (cursor.moveToFirst()) {
            qianming = cursor.getString(cursor.getColumnIndex("qianming"));
        }
        cursor.close();
        return qianming;
    }

    public String getPassWord() {
        String password = null;
        Cursor cursor = db.query("user", null, "online = ?", new String[]{"1"}, null, null, null, null);
        if (cursor.moveToFirst()) {
            password = cursor.getString(cursor.getColumnIndex("password"));
        }
        cursor.close();
        return password;
    }

    //返回头像的
    public String getHeadPhoto() {
       String headphoto = null;
        Cursor cursor = db.query("user", null, "online = ?", new String[]{"1"}, null, null, null, null);
        if (cursor.moveToFirst()) {
            headphoto = cursor.getString(cursor.getColumnIndex("headphoto"));
        }
        cursor.close();
        return headphoto;
    }

    //返回头像的
    public String getBeiJing() {
        String beijing = null;
        Cursor cursor = db.query("user", null, "online = ?", new String[]{"1"}, null, null, null, null);
        if (cursor.moveToFirst()) {
            beijing = cursor.getString(cursor.getColumnIndex("beijing"));
        }
        cursor.close();
        return beijing;
    }

    public String getBeiJingForName(String name) {
        String beijing = null;
        Cursor cursor = db.query("user", null, "name = ?", new String[]{name}, null, null, null, null);
        if (cursor.moveToFirst()) {
            beijing = cursor.getString(cursor.getColumnIndex("beijing"));
        }
        cursor.close();
        return beijing;
    }
    public void addcomments(String comments, int id) {
        db.execSQL("UPDATE tuwen SET comments = ? WHERE id = ?  ",new Object[]{comments,id} );
    }



    public ArrayList<String> getCommentList() {
        ArrayList<String> list = new ArrayList<>();
        Cursor cursor = db.query("tuwen", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String comments = cursor.getString(cursor.getColumnIndex("comments"));
            list.add(comments);
        }
        cursor.close();
        return list;
    }


}