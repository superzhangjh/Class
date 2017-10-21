package com.example.a731.aclass.data;

import org.litepal.crud.DataSupport;

/**
 * Created by 郑选辉 on 2017/10/21.
 */

public class SysNotification extends DataSupport{

    public static final int APPLY_JOIN_GROUP = 201;
    public static final int APPLY_FRIEND = 202;
    public static final int INVITED_GROUP = 203;

    public static final int NEED_DEAL = 301;
    public static final int ACCEPTED = 302;
    public static final int REFUSED = 303;

    private String groupId;
    private String username;
    private String message;
    private int type;
    private int statue;

    public int getStatue() {
        return statue;
    }

    public void setStatue(int statue) {
        this.statue = statue;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
