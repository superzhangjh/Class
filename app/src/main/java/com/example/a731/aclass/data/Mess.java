package com.example.a731.aclass.data;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class Mess {
    private String creatorID;//消息发出者
    private String name;//
    private String message;//消息内容
    private String date;//日期
    private Boolean isGroupMess;

    public Boolean getGroupMess() {
        return isGroupMess;
    }

    public void setGroupMess(Boolean groupMess) {
        isGroupMess = groupMess;
    }

    public String getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(String creatorID) {
        this.creatorID = creatorID;
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
