package com.example.a731.aclass.presenter.impl;

import android.util.Log;

import com.example.a731.aclass.data.Mess;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.ChatPresenter;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.util.ThreadUtils;
import com.example.a731.aclass.view.ChatView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by 郑选辉 on 2017/10/9.
 */

public class ChatPresenterImpl implements ChatPresenter {

    private ChatView mChatView;
    private List<Mess> messages = new ArrayList<>();
    private String mUserName;

    public ChatPresenterImpl(ChatView chatView){
        mChatView = chatView;
        mUserName = BmobUser.getCurrentUser(Users.class).getUsername();
    }

    @Override
    public void senMessage(final String username, final String content, final int chatType) {
        final EMMessage message = EMMessage.createTxtSendMessage(content, username);
        if (chatType == EaseMobUtil.CHATTYPE_GROUP)
            message.setChatType(EMMessage.ChatType.GroupChat);
        Mess mess = new Mess();
        mess.setCreatorID(mUserName);
        mess.setMessage(content);
        mess.setDate(message.localTime());
        messages.add(mess);
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                EaseMobUtil.sendTextMessage(message, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mChatView.onSendMessageSuccess();
                            }
                        });
                    }

                    @Override
                    public void onError(final int code, final String error) {
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mChatView.onSendMessageFail(code+":"+error);
                            }
                        });

                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }
                });
            }
        });
    }

    public void getConversationRecord(final String username){
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                EMConversation conversation = EaseMobUtil.getConversation(username);
                if (conversation == null) return;
                List<EMMessage> emMessages = conversation.getAllMessages();


                for (int i=0; i<emMessages.size();i++){
                    EMMessage msg = emMessages.get(i);
                    Mess mess = new Mess();
                    mess.setCreatorID(msg.getFrom());
                    mess.setDate(msg.getMsgTime());
                    Log.i("ChatPresenterImpl-time",msg.getMsgTime()+"");
                    switch(msg.getType()){
                        //文本信息
                        case TXT:
                            EMTextMessageBody body = (EMTextMessageBody) msg.getBody();
                            mess.setMessage(body.getMessage());
                            messages.add(mess);
                            break;
                        //图片信息
                        case IMAGE:
                            break;
                        //视频
                        case VIDEO:
                            break;
                        //位置
                        case LOCATION:
                            break;
                        //声音
                        case VOICE:
                            break;
                        //文件
                        case FILE:
                            break;
                    }
                }
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mChatView.onGetRecordSuccess();
                    }
                });
            }
        });

    }

    public List<Mess> getMessage(){
        return messages;
    }


}
