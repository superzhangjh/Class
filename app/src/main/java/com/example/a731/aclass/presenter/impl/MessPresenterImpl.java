package com.example.a731.aclass.presenter.impl;

import android.util.Log;

import com.example.a731.aclass.data.BasicMessage;
import com.example.a731.aclass.data.Conversation;
import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.MessPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.util.ThreadUtils;
import com.example.a731.aclass.view.MessView;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 郑选辉 on 2017/10/14.
 */

public class MessPresenterImpl implements MessPresenter {
    private MessView mMessView;

    public MessPresenterImpl(MessView messView){
        mMessView = messView;
    }

    @Override
    public void getConversations() {


        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                final List<Conversation> conversations = new ArrayList<>();
                Map<String, EMConversation> emConversationMap = EaseMobUtil.getAllConversations();
                List<EMConversation> mEMConversations = new ArrayList<>();
                mEMConversations.addAll(emConversationMap.values());
                Collections.sort(mEMConversations, new Comparator<EMConversation>() {
                    @Override
                    public int compare(EMConversation o1, EMConversation o2) {
                        return (int) (o2.getLastMessage().getMsgTime() - o1.getLastMessage().getMsgTime());
                    }
                });

                for (EMConversation emConversation:mEMConversations){
                    Conversation conversation = new Conversation();
                    List<BasicMessage> messages = DataSupport.where("userId = ?",emConversation.conversationId()).find(BasicMessage.class);
                    if (messages.size() == 0) continue;
                    BasicMessage mess = messages.get(0);
                    conversation.setLastMess(((EMTextMessageBody) emConversation.getLastMessage().getBody()).getMessage());
                    conversation.setChatType(emConversation.getType());
                    Log.e("MessPresenterImpl",((EMTextMessageBody) emConversation.getLastMessage().getBody()).getMessage());
                    conversation.setImgHead(mess.getHeadImg());
                    if (mess.getName() != null){
                        conversation.setName(mess.getName());
                    }
                    conversation.setChatId(emConversation.conversationId());
                    conversations.add(conversation);
                }


                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mMessView.onGetConversationSuccess(conversations);
                    }
                });
            }
        });
    }
}