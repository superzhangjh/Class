package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.data.Mess;
import com.example.a731.aclass.presenter.ChatPresenter;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.util.ThreadUtils;
import com.example.a731.aclass.view.ChatView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郑选辉 on 2017/10/9.
 */

public class ChatPresenterImpl implements ChatPresenter {

    private ChatView mChatView;
    private List<Mess> messages = new ArrayList<>();

    public ChatPresenterImpl(ChatView chatView){
        mChatView = chatView;
    }

    @Override
    public void senMessage(final String username, final String content) {
        Mess mess = new Mess();
        mess.setCreatorID("myself");
        mess.setMessage(content);
        messages.add(mess);
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                EaseMobUtil.sendTextMessage(content, username, EaseMobUtil.CHATTYPE_PERSONAL, new EMCallBack() {
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
                    public void onError(int code, String error) {
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mChatView.onSendMessageFail();
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

    public EMMessage getConversationLastRecord(String username){
        return EaseMobUtil.getConversationLastRecord(username);
    }

    public void getConversationRecord(final String username){
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                EMMessage msgs = getConversationLastRecord(username);
                if (msgs == null) return;
                String lastMsgId = msgs.getMsgId();
                List<EMMessage> emMessages = EaseMobUtil.getConversationRecord(username,lastMsgId);
                emMessages.add(msgs);

                for (int i=0; i<emMessages.size();i++){
                    EMMessage msg = emMessages.get(i);
                    Mess mess = new Mess();
                    mess.setCreatorID(msg.getFrom());
                    mess.setDate(msg.getMsgTime()+"");
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
