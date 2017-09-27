package com.example.a731.aclass.presenter.impl;

import android.os.Message;
import android.util.Log;

import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.LoginPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.view.LoginView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * Created by 郑选辉 on 2017/9/27.
 */

public class LoginPresenterImpl implements LoginPresenter{
    private LoginView mView;
    public LoginPresenterImpl(LoginView view){
        mView=view;
    }

    @Override
    public void onCheckLogin(String phoneNum, String password) {
        BmobUtil.Login(phoneNum, password, new LogInListener<Users>() {
            @Override
            public void done(Users users, BmobException e) {
                if (e == null){
                    mView.onLoginSuccess();
                }else{
                    mView.onLoginFail(e.getMessage());
                }
            }
        });
        //现在登录成功

    }
}
