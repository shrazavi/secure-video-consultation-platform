package com.shrazavi.dadmehr.DataClass;

public class Post {
    String _id;
    String username;
    String text;
    String img;
//    Boolean like;
//    int nazar;
    int like;

    public String getId() {
        return _id;
    }

    public void setId(String id) {
        this._id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
