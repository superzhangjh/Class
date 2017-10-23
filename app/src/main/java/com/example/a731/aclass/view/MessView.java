package com.example.a731.aclass.view;

import com.example.a731.aclass.data.Conversation;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/10/15.
 */

public interface MessView {
    void onGetConversationSuccess(List<Conversation> conversations);

    void onGetConversationFail(String msg);
}
