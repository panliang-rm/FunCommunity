package com.example.hello.data;

import java.io.Serializable;

/**
 * 评论用户类*/
public class UserBean implements Serializable {

    private String userName;



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}