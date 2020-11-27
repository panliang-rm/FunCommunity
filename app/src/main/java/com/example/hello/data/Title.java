package com.example.hello.data;

/**
 * 图文类*/
public class Title {

    private String title;
    private int id;
    private String photo;
    private String user_id;
    private String headphoto;
    private String comments;
    private String like;
    private String time;



    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getHeadphoto() {
        return headphoto;
    }

    public void setHeadphoto(String headphoto) {
        this.headphoto = headphoto;
    }


    public Title(String title, int id, String photo, String user_id, String headphoto, String comments, String like, String time) {
        this.title = title;
        this.id = id;
        this.photo = photo;
        this.user_id = user_id;
        this.headphoto = headphoto;
        this.comments = comments;
        this.like = like;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }





    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
       this.id = id;
    }

    public String getTitle(){
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
