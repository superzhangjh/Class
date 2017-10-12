package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.presenter.ChatPresenter;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.view.ChatView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMMessage;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/10/9.
 */

public class ChatPresenterImpl implements ChatPresenter {

    private ChatView mChatView;

    public ChatPresenterImpl(ChatView chatView){
        mChatView = chatView;
    }

    @Override
    public void senMessage(String username,String content) {
        EaseMobUtil.sendTextMessage(content, username, EaseMobUtil.CHATTYPE_PERSONAL, new EMCallBack() {
            @Override
            public void onSuccess() {
                mChatView.onSendMessageSuccess();
            }

            @Override
            public void onError(int code, String error) {
                mChatView.onSendMessageFail();
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    public void getConversationRecord(String username){

        EMMessage msgs = EaseMobUtil.getConversationLastRecord(username);
        if(msgs== null) return;

        String lastMsgId = msgs.getMsgId();

        List<EMMessage> emMessages = EaseMobUtil.getConversationRecord(username,lastMsgId);
        if (emMessages!=null){
            mChatView.onGetRecordSuccess(emMessages);
        }else{

        }
    }


}
