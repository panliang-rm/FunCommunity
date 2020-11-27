package com.example.hello.data;

/**
 * 登录用户类*/
public class User {
    public  String name;            //用户名
    private String password;        //密码
    private int id;
    private String headphoto;
    private String online;
    private String qianming;


    public String getQianming() {
        return qianming;
    }

    public void setQianming(String qianming) {
        this.qianming = qianming;
    }

    public String getOnline() {
        return online;
    }

    public User(String name, String password, String online) {
        this.name = name;
        this.password = password;

        this.online = online;
    }

    public void setOnline(String online) {
        this.online = online;
    }



    public String getHeadphoto() {
        return headphoto;
    }

    public void setHeadphoto(String headphoto) {
        this.headphoto = headphoto;
    }

    public User() {

    }

    public String getName() {return name ;}
    public void setName(String name) {
        this.name = name;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id + '\'' +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }


}
