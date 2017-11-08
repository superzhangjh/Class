package com.example.a731.aclass.presenter;

import com.example.a731.aclass.data.Mess;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/10/9.
 */

public interface ChatPresenter {
    void senMessage(String username, String content, int chatType);

    void getConversationRecord(String username);


    List<Mess> getMessage();

    void loadMoreMessages(String chatToId);
}

