package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.data.Conversation;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.MessPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.view.MessView;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMTextMessageBody;

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
    private List<Conversation> conversations;
    private Conversation conversation;

    public MessPresenterImpl(MessView messView){
        mMessView = messView;

    }

    @Override
    public void getConversations() {
        conversations = new ArrayList<>();
        Map<String,EMConversation> conversationMap = EaseMobUtil.getAllConversations();
        if (conversationMap==null)return;
        for (Map.Entry<String,EMConversation> entry:conversationMap.entrySet()){
            EMConversation emConversation = entry.getValue();
            conversation = new Conversation();
            conversation.setName(emConversation.conversationId());
            BmobUtil.queryUser(emConversation.conversationId(), new FindListener<Users>() {
                @Override
                public void done(List<Users> list, BmobException e) {
                    if(list.size()>0){
                        conversation.setImgHead(list.get(0).getHeadImg());
                    }
                }
            });
            EMTextMessageBody mess = (EMTextMessageBody) emConversation.getLastMessage().getBody();
            conversation.setLastMess(mess.getMessage());
            conversation.setChatType(emConversation.getType());
            conversations.add(conversation);
        }
        if (conversations.size()>0){
            mMessView.onGetConversationSuccess(conversations);
        }else{
            mMessView.onGetConversationFail();
        }
    }
}
