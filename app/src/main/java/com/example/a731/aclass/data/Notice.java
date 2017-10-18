package com.example.a731.aclass.data;

import java.io.File;

/**
 * Created by Administrator on 2017/10/2/002.
 */

public class Notice{
    private Users creator;//创建者
    private String title;//标题

    public Users getCreator() {
        return creator;
    }

    public void setCreator(Users creator) {
        this.creator = creator;
    }

    private String content;//内容
    private String date;//创建日期
    private String[] photoList;//图片链接
    private String[] respond;//回应
    private Commend commend;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getRespond() {
        return respond;
    }

    public void setRespond(String[] respond) {
        this.respond = respond;
    }

    public Commend getCommend() {
        return commend;
    }

    public void setCommend(Commend commend) {
        this.commend = commend;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String[] getPhotoList() {
        return photoList;
    }

    public void setPhotoList(String[] photoList) {
        this.photoList = photoList;
    }
}
