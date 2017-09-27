package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.SignUpPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.view.SignUpView;

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

            }
        });
    }

    @Override
    public void register(String userName, String phoneNum, String password, String smsCode) {
        BmobUtil.register(userName, phoneNum, password, smsCode, new SaveListener<Users>() {
            @Override
            public void done(Users users, BmobException e) {
                if (e == null){
                    mSignUpView.onRegisterSuccess();
                }else{
                    mSignUpView.onRegisterFail(e.getMessage());
                }
            }
        });
    }
}
