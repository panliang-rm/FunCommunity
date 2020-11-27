package com.example.hello.data;

import java.io.Serializable;

public class CommentsBean implements Serializable {

    private int id;
    private String content;
    private UserBean replyUser; // 回复人信息
    private UserBean commentsUser;  // 评论人信息


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserBean getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(UserBean replyUser) {
        this.replyUser = replyUser;
    }

    public UserBean getCommentsUser() {
        return commentsUser;
    }

    public void setCommentsUser(UserBean commentsUser) {
        this.commentsUser = commentsUser;
    }
}

