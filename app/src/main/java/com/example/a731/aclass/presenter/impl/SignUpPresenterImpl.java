package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.SignUpPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.util.ThreadUtils;
import com.example.a731.aclass.view.SignUpView;
import com.hyphenate.exceptions.HyphenateException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 郑选辉 on 2017/9/27.
 */

public class SignUpPresenterImpl implements SignUpPresenter{

    private SignUpView mSignUpView;

    public SignUpPresenterImpl(SignUpView signUpView){
        mSignUpView = signUpView;
    }

    @Override
    public void requestSMSCode(String phoneNum) {
        BmobUtil.requestSMSCode(phoneNum, new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    mSignUpView.onRequestSMSCodeSuccess();
                }else{
                    mSignUpView.onRequestSMSCodeFail("bmob:"+e.getMessage());
                }
            }
        });
    }

    @Override
    public void register(final String userName, String phoneNum, final String password, String smsCode) {
        BmobUtil.register(userName, phoneNum, password, smsCode, new SaveListener<Users>() {
            @Override
            public void done(Users users, BmobException e) {
                if (e == null){
                    registerEaseMob(userName,password);
                }else{
                    mSignUpView.onRegisterFail("Bomb:"+e.getMessage());
                }
            }
        });
    }

    private void registerEaseMob(String userName, String password) {
        try {
            EaseMobUtil.signUp(userName,password);
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSignUpView.onRegisterSuccess();
                }
            });
        } catch (final HyphenateException e) {
            e.printStackTrace();
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSignUpView.onRegisterFail("Mob:"+e.getMessage());
                }
            });
        }
    }
}
