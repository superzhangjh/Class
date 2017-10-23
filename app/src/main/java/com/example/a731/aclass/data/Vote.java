package com.example.a731.aclass.data;

import java.util.List;

/**
 * Created by Administrator on 2017/10/2/002.
 */

public class Vote {
    private Users creator;//创建者
    private int type;//投票类型
    private String title;//标题
    private String content;//内容
    private String date;//创建日期
    private String expirationDate;//截止日期
    private String[] photoList;//图片链接
    private List<Item> voteItems;//投票选项

    public Users getCreator() {
        return creator;
    }

    public void setCreator(Users creator) {
        this.creator = creator;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String[] getPhotoList() {
        return photoList;
    }

    public void setPhotoList(String[] photoList) {
        this.photoList = photoList;
    }

    public List<Item> getVoteItems() {
        return voteItems;
    }

    public void setVoteItems(List<Item> voteItems) {
        this.voteItems = voteItems;
    }

    public static class Item{
        private String itemContent;//选项内容
        private int itemCount;//票数
        private int[] itemSelectId;//投票者id

        public String getItemContent() {
            return itemContent;
        }

        public void setItemContent(String itemContent) {
            this.itemContent = itemContent;
        }

        public int getItemCount() {
            return itemCount;
        }

        public void setItemCount(int itemCount) {
            this.itemCount = itemCount;
        }

        public int[] getItemSelectId() {
            return itemSelectId;
        }

        public void setItemSelectId(int[] itemSelectId) {
            this.itemSelectId = itemSelectId;
        }
    }
}
