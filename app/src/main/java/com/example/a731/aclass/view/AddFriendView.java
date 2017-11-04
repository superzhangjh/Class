package com.example.a731.aclass.view;

import com.example.a731.aclass.data.Users;

/**
 * Created by Administrator on 2017/11/4/004.
 */

public interface AddFriendView {
    void onGetUserSuccess(Users users);

    void onGetUserFail(String message);
}
