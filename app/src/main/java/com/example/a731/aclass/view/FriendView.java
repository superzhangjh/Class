package com.example.a731.aclass.view;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/10/9.
 */

public interface FriendView {
    void onGetFriendsSuccess(List<String> username);

    void onGetFriendsFail(String message);
}
