package com.example.a731.aclass.view;

import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Users;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/9/27.
 */

public interface MainView {
    void onLogoutFail(String error);

    void onDisconnected(int errorCode);

    void onLogoutSuccess();

    void onGetFriendsSuccess(List<Users> friendList);

    void onGetFriendsFail(String s);

    void onGetGroupFail(String message);

    void onGetGroupSuccess(List<Group> groupList);
}
