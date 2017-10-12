package com.example.a731.aclass.view;

import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/10/9.
 */

public interface ChatView {
    void onSendMessageSuccess();

    void onSendMessageFail();

    void onGetRecordSuccess(List<EMMessage> emMessages);
}
