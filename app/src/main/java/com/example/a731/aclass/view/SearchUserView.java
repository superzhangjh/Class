package com.example.a731.aclass.view;

import com.example.a731.aclass.data.Users;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/10/9.
 */

public interface SearchUserView {
    void onSearchUserSuccess(List<Users> list);

    void onSearchuserFail(String message);

    void onAddFriendSuccess();

    void onAddFriendFail(String message);
}
