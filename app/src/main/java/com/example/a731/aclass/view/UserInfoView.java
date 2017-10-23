package com.example.a731.aclass.view;

import com.example.a731.aclass.data.Users;

/**
 * Created by 郑选辉 on 2017/10/22.
 */

public interface UserInfoView {
    void onGetUserSuccess(Users users);

    void onGetUserFail(String message);
}
