package com.example.a731.aclass.adapter;
import com.hyphenate.EMContactListener;

/**
 * Created by 郑选辉 on 2017/10/21.
 */

public class EMContactListenerAdapter implements EMContactListener{
    @Override
    public void onContactAdded(String username) {
        //好友请求被同意
    }

    @Override
    public void onContactDeleted(String username) {
        //好友请求被拒绝
    }

    @Override
    public void onContactInvited(String username, String reason) {
        //收到好友邀请
    }
    @Override
    public void onFriendRequestAccepted(String username) {
        //同意邀请时回调此方法
    }
    @Override
    public void onFriendRequestDeclined(String username) {
        //拒绝时回调此方法
    }
}

