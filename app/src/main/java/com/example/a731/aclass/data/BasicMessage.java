package com.example.a731.aclass.data;

import org.litepal.crud.DataSupport;

/**
 * Created by 郑选辉 on 2017/11/6.
 */

public class BasicMessage extends DataSupport {

    public static final int GROUP = 301;
    public static final int USER = 302;

    private int type;
    private String userId;
    private String name;
    private String headImg;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }
}
