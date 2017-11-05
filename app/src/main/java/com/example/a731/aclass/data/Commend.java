package com.example.a731.aclass.data;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/10/16/016.
 */

public class Commend implements Serializable {
    private Users users;//评论者
    private String date;//评论时间
    private String content;//内容
    private String like;//点赞
    private String BmobId;//根据上传到网上的数据返回的BmobID判断该评论的归属位置,打开评论页面时

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

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    private Vote vote;

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public Vote getVote() {

        return vote;
    }
}
