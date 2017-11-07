package com.example.a731.aclass.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/10/16/016.
 */

public class Commend implements Serializable {
    private List<String> likeList;//点赞
    private Users users;//评论者
    private String date;//评论时间
    private String content;//内容
    private News news;
    private Vote vote;

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public Vote getVote() {
        return vote;
    }

    public void setVote(Vote vote) {
        this.vote = vote;
    }

    public List<String> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<String> likeList) {
        this.likeList = likeList;
    }

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
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
}
