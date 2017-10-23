package com.example.a731.aclass.data;


import cn.bmob.v3.BmobUser;

/**
 * Created by 郑选辉 on 2017/9/19.
 */

public class Users extends BmobUser{
    //bmob已经提供了用户表,只需添加额外需要的字段就可以了
//    private String username; 用户名
//    private String password; 密码
//    private String mobilePhoneNumber; 手机号码
    private String name;//姓名
    private int id;//学号
    private int sex;//性别(1代表男，2代表女)
    private String project;//专业
    private String homeLand;//籍贯
    private String intro;//简介
    private String headImg;//头像

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getHomeLand() {
        return homeLand;
    }

    public void setHomeLand(String homeLand) {
        this.homeLand = homeLand;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }


}
