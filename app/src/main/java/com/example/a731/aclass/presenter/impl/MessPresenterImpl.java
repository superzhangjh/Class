package com.example.a731.aclass.presenter.impl;

import android.util.Log;

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
        if (conversations!=null){
            conversations.clear();
        }
        conversations = new ArrayList<>();
        Map<String,EMConversation> conversationMap = EaseMobUtil.getAllConversations();
        if (conversationMap==null)return;
        for (Map.Entry<String,EMConversation> entry:conversationMap.entrySet()){
            Log.i("MesspresenterImpl","开始获取会话"+conversationMap.size());
            final EMConversation emConversation = entry.getValue();
            conversation = new Conversation();
            if (emConversation.getType() == EMConversation.EMConversationType.Chat){
                BmobUtil.queryUser(emConversation.conversationId(), new FindListener<Users>() {
                    @Override
                    public void done(List<Users> list, BmobException e) {
                        if (e == null){
                            conversation.setImgHead(list.get(0).getHeadImg());
                            EMTextMessageBody mess = (EMTextMessageBody) emConversation.getLastMessage().getBody();
                            conversation.setLastMess(mess.getMessage());
                            conversation.setChatType(emConversation.getType());
                            if (list.get(0).getName()!=null){
                                conversation.setName(list.get(0).getName());
                            }else{
                                conversation.setName(list.get(0).getUsername());
                            }

                            if (!conversations.contains(conversation))
                                conversations.add(conversation);
                            Log.i("MesspresenterImpl","获取到个人会话"+conversations.size());
                            mMessView.onGetConversationSuccess(conversations);

                        }else{
                            mMessView.onGetConversationFail("获取会话失败");
                        }
                    }
                });
            }else if (emConversation.getType() == EMConversation.EMConversationType.GroupChat){
                BmobUtil.getGroupByField("groupId", emConversation.conversationId(), new FindListener<Group>() {
                    @Override
                    public void done(List<Group> list, BmobException e) {
                        if (e == null){
                            conversation.setImgHead(list.get(0).getHeadImg());
                            EMTextMessageBody mess = (EMTextMessageBody) emConversation.getLastMessage().getBody();
                            conversation.setLastMess(mess.getMessage());
                            conversation.setChatType(emConversation.getType());
                            conversation.setName(list.get(0).getName());
                            conversations.add(conversation);
                            mMessView.onGetConversationSuccess(conversations);
                            Log.i("MesspresenterImpl","获取到群组会话"+conversations.size());
                        }else{
                            mMessView.onGetConversationFail("获取会话失败");
                        }
                    }
                });
            }
        }
    }
}