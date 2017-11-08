package com.example.a731.aclass.presenter.impl;

import android.util.Log;

import com.example.a731.aclass.data.Mess;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.ChatPresenter;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.util.ThreadUtils;
import com.example.a731.aclass.view.ChatView;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
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
    private boolean hasMoreData = true;
    private String firstId;
    public static final int DEFAULT_PAGE_SIZE = 20;

    public ChatPresenterImpl(ChatView chatView){
        mChatView = chatView;
        mUserName = BmobUser.getCurrentUser(Users.class).getUsername();
    }

    @Override
    public void senMessage(final String username, final String content, final int chatType) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                final EMMessage message = EMMessage.createTxtSendMessage(content, username);
                if (chatType == EaseMobUtil.CHATTYPE_GROUP)
                    message.setChatType(EMMessage.ChatType.GroupChat);
                Mess mess = new Mess();
                mess.setCreatorID(mUserName);
                mess.setMessage(content);
                mess.setDate(message.localTime());
                messages.add(mess);
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
        Log.e("ChatPresenterImpl","获取聊天记录");
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
                if (conversation == null) return;
                conversation.markAllMessagesAsRead();
                List<EMMessage> emMessages = conversation.getAllMessages();
                firstId = emMessages.get(0).getMsgId();

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
                Log.i("ChatPresenterImpl","获取到聊天记录数量:"+messages.size());
                if (conversation.getAllMsgCount()>messages.size()){
                    loadMoreMessages(username);
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

    @Override
    public void loadMoreMessages(final String userName) {
        if (hasMoreData) {
            ThreadUtils.runOnBackgroundThread(new Runnable() {
                @Override
                public void run() {
                    EMConversation conversation = EMClient.getInstance().chatManager().getConversation(userName);
                    //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
                    //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
                    final List<EMMessage> emMessages = conversation.loadMoreMsgFromDB(firstId, DEFAULT_PAGE_SIZE);
                    if (emMessages.size()<2){
                        hasMoreData = false;
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mChatView.onNoMoreData();
                            }
                        });
                        return;
                    }
                    firstId = emMessages.get(0).getMsgId();
                    hasMoreData = (messages.size() == DEFAULT_PAGE_SIZE);
                    for (int i=0; i<emMessages.size();i++){
                        EMMessage msg = emMessages.get(i);
                        Mess mess = new Mess();
                        mess.setCreatorID(msg.getFrom());
                        mess.setDate(msg.getMsgTime());
                        switch(msg.getType()){
                            //文本信息
                            case TXT:
                                EMTextMessageBody body = (EMTextMessageBody) msg.getBody();
                                mess.setMessage(body.getMessage());
                                messages.add(0,mess);
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
                            mChatView.onMoreMessagesLoaded(emMessages.size());
                        }
                    });
                }
            });
        } else {
            mChatView.onNoMoreData();
        }
    }

    public List<Mess> getMessage(){
        return messages;
    }


}
