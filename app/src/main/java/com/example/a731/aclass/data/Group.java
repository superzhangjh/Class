package com.example.a731.aclass.data;

import java.io.File;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by 郑选辉 on 2017/9/20.
 */

public class Group extends BmobObject {
    private String name;//圈名
    private Users creator;//创建者
    private BmobRelation member;//成员
    private BmobRelation administrator;//管理员
    private int number;//成员数
    private File headImg;//头像
    private String notification;//通知
    private BmobFile images;//上传图片
    private String vote;//投票

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Users getCreator() {
        return creator;
    }

    public void setCreator(Users creator) {
        this.creator = creator;
    }

    public BmobRelation getMember() {
        return member;
    }

    public void setMember(BmobRelation member) {
        this.member = member;
    }

    public BmobRelation getAdministrator() {
        return administrator;
    }

    public void setAdministrator(BmobRelation administrator) {
        this.administrator = administrator;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public File getHeadImg() {
        return headImg;
    }

    public void setHeadImg(File headImg) {
        this.headImg = headImg;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public BmobFile getImages() {
        return images;
    }

    public void setImages(BmobFile images) {
        this.images = images;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }
}
