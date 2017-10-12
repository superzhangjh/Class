package com.example.a731.aclass.presenter;

/**
 * Created by 郑选辉 on 2017/10/9.
 */

public interface ChatPresenter {
    void senMessage(String username, String content);

    void getConversationRecord(String username);


}
