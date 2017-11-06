package com.example.a731.aclass.presenter.impl;

import android.util.Log;

import com.example.a731.aclass.data.BasicMessage;
import com.example.a731.aclass.data.Conversation;
import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.MessPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.view.MessView;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
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
        List<Conversation> conversations = new ArrayList<>();
        Map<String,EMConversation> conversationMap = EaseMobUtil.getAllConversations();
        if (conversationMap==null)return;
        for (Map.Entry<String,EMConversation> entry:conversationMap.entrySet()){
            Log.i("MesspresenterImpl","开始获取会话"+conversationMap.size());
            final EMConversation emConversation = entry.getValue();
            Conversation conversation = new Conversation();

            List<BasicMessage> messages = DataSupport.where("userId = ?",emConversation.conversationId()).find(BasicMessage.class);
            if (messages.size() == 0) continue;
            BasicMessage mess = messages.get(0);
            EMTextMessageBody messageBody = (EMTextMessageBody) emConversation.getLastMessage().getBody();
            conversation.setChatType(emConversation.getType());
            conversation.setLastMess(messageBody.getMessage());
            conversation.setImgHead(mess.getHeadImg());
            if (mess.getName() != null){
                conversation.setName(mess.getName());
            }else{
                conversation.setName(mess.getUserId());
            }
            conversations.add(conversation);
        }
        mMessView.onGetConversationSuccess(conversations);
    }
}