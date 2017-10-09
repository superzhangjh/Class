package com.example.a731.aclass.presenter.impl;


import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.LoginPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.util.ThreadUtils;
import com.example.a731.aclass.view.LoginView;
import com.hyphenate.EMCallBack;

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
    public void onCheckLogin(final String username, final String password) {
        BmobUtil.Login(username, password, new LogInListener<Users>() {
            @Override
            public void done(Users users, BmobException e) {
                if (e == null){
                    easeMobLogin(username,password);
                }else{
                    mView.onLoginFail("Bomb:"+e.getMessage());
                }
            }
        });
        //现在登录成功

    }

    private void easeMobLogin(String username,String password) {
        EaseMobUtil.login(username, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.onLoginSuccess();
                    }
                });
            }

            @Override
            public void onError(int i, final String s) {
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mView.onLoginFail("Mob:"+s);
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
}
