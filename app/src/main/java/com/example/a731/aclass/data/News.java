package com.example.a731.aclass.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/10/2/002.
 */

public class News implements Serializable {
    private List<String> photoList;//链接
    private String date;//创建日期
    private String content;//相片描述
    private Users users;
    private int like;

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public List<String> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<String> photoList) {
        this.photoList = photoList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }
}
