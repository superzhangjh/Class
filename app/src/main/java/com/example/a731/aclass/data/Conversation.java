package com.example.a731.aclass.data;

import com.hyphenate.chat.EMConversation;

/**
 * Created by 郑选辉 on 2017/10/15.
 */

public class Conversation {
    private String name;
    private String chatId;
    private String imgHead;
    private String lastMess;
    private EMConversation.EMConversationType chatType;

    public EMConversation.EMConversationType getChatType() {
        return chatType;
    }

    public void setChatType(EMConversation.EMConversationType chatType) {
        this.chatType = chatType;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgHead() {
        return imgHead;
    }

    public void setImgHead(String imgHead) {
        this.imgHead = imgHead;
    }

    public String getLastMess() {
        return lastMess;
    }

    public void setLastMess(String lastMess) {
        this.lastMess = lastMess;
    }
}
