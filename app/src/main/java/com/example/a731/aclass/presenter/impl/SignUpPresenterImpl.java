package com.example.a731.aclass.presenter.impl;

import android.util.Log;

import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.SignUpPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.util.ThreadUtils;
import com.example.a731.aclass.view.SignUpView;
import com.hyphenate.EMCallBack;
import com.hyphenate.exceptions.HyphenateException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 郑选辉 on 2017/9/27.
 */

public class SignUpPresenterImpl implements SignUpPresenter{

    private SignUpView mSignUpView;
    private static final String TAG = "SignupPresenterImpl";

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
                    Log.i(TAG,e.getMessage()+"--"+e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void register(final String userName, final String phoneNum, final String password, final String smsCode) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EaseMobUtil.signUp(userName,password);
                    registerBmob(userName,phoneNum,password,smsCode);
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSignUpView.onRegisterFail("Mob:"+e.getMessage()+e.getErrorCode());
                        }
                    });
                }
            }
        });
    }

    private void registerBmob(final String userName, String phoneNum, final String password, String smsCode) {
        BmobUtil.register(userName, phoneNum, password, smsCode, new SaveListener<Users>() {
            @Override
            public void done(Users users, final BmobException e) {
                if (e == null){
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSignUpView.onRegisterSuccess();
                            EaseMobUtil.login(userName, password, new EMCallBack() {
                                @Override
                                public void onSuccess() {
                                    mSignUpView.onRegisterSuccess();
                                }

                                @Override
                                public void onError(int code, String error) {

                                }

                                @Override
                                public void onProgress(int progress, String status) {

                                }
                            });
                        }
                    });
                }else{
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSignUpView.onRegisterFail("Bmob:"+e.getMessage()+e.getErrorCode());
                        }
                    });
                }
            }
        });
    }
}
