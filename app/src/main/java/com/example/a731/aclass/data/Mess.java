package com.example.a731.aclass.data;

import android.media.Image;

import java.util.Date;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class Mess {
    private String origin;//消息发出者
    private String name;//
    private String message;//消息内容
    private String date;//日期
    private int orientation;//0或1，0代表己方，1代表对方

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
