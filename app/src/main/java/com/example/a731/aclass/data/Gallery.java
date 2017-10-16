package com.example.a731.aclass.data;

/**
 * Created by Administrator on 2017/10/2/002.
 */

public class Gallery {
    //照片集
    private String[] srcList;//链接
    private String date;//创建日期
    private String intro;//相片描述
    private String creatorID;//创建者
    private String creatorName;//创建者名称
    private int like;//点赞

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

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

}
