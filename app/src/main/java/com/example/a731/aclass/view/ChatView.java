package com.example.a731.aclass.view;

/**
 * Created by 郑选辉 on 2017/10/9.
 */

public interface ChatView {
    void onSendMessageSuccess();

    void onSendMessageFail(String msg);

    void onGetRecordSuccess();

    void onMoreMessagesLoaded(int size);

    void onNoMoreData();

}
