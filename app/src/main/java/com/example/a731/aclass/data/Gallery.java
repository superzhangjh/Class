package com.example.a731.aclass.data;

/**
 * Created by Administrator on 2017/10/2/002.
 */

public class Gallery {
    //照片集
    private String[] srcList;//链接
    private String date;//创建日期
    private String createrID;//创建者

    public String[] getSrcList() {
        return srcList;
    }

    public void setSrcList(String[] srcList) {
        this.srcList = srcList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCreaterID() {
        return createrID;
    }

    public void setCreaterID(String createrID) {
        this.createrID = createrID;
    }
}
