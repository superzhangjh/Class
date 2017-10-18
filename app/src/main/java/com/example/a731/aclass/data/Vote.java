package com.example.a731.aclass.data;

/**
 * Created by Administrator on 2017/10/2/002.
 */

public class Vote {
    private Users creator;//创建者
    private String title;//标题
    private String content;//内容
    private String date;//创建日期
    private String[] photoList;//图片链接
    private String[] voteItems;//投票选项
    private int[] itemRates;//各组票数

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int[] getItemRates() {
        return itemRates;
    }

    public void setItemRates(int[] itemRates) {
        this.itemRates = itemRates;
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

    public String[] getVoteItems() {
        return voteItems;
    }

    public void setVoteItems(String[] voteItems) {
        this.voteItems = voteItems;
    }

    public Users getCreator() {
        return creator;
    }

    public void setCreator(Users creator) {
        this.creator = creator;
    }
}
