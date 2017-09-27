package com.example.a731.aclass.data;

import android.media.Image;

import java.util.Date;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class Mess {
    private String name;//
    private String message;//消息内容
    private String date;//日期
    private int origin;//0或1，0代表己方，1代表对方

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

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
