package com.example.a731.aclass.data;

/**
 * Created by Administrator on 2017/10/16/016.
 */

public class Commend {
    private String Id;//评论者id
    private String imgHead;//评论者头像
    private String date;//评论时间
    private String content;//内容

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImgHead() {
        return imgHead;
    }

    public void setImgHead(String imgHead) {
        this.imgHead = imgHead;
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

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    private String like;//点赞
}
