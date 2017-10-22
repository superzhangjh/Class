package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.UserInfoPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.view.UserInfoView;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 郑选辉 on 2017/10/22.
 */

public class UserInfoPresenterImpl implements UserInfoPresenter {
    private UserInfoView mUserInfoView;
    public UserInfoPresenterImpl(UserInfoView userInfoView){
        mUserInfoView = userInfoView;
    }

    @Override
    public void getUser(String username) {
        BmobUtil.queryUser(username, new FindListener<Users>() {
            @Override
            public void done(List<Users> list, BmobException e) {
                if (e==null){
                    mUserInfoView.onGetUserSuccess(list.get(0));
                }else{
                    mUserInfoView.onGetUserFail(e.getMessage());
                }
            }
        });
    }
}
