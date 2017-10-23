package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.presenter.SplashPresenter;
import com.example.a731.aclass.view.SplashView;
import com.hyphenate.chat.EMClient;

/**
 * Created by 郑选辉 on 2017/10/12.
 */

public class SplashPresenterImpl implements SplashPresenter {
    private SplashView mSplashView;
    public SplashPresenterImpl(SplashView splashView){
        mSplashView = splashView;
    }

    @Override
    public void checkLoginStatus() {
        if (EMClient.getInstance().isLoggedInBefore() && EMClient.getInstance().isConnected()){
            mSplashView.onLoggedIn();
        }else{
            mSplashView.onNotLogin();
        }
    }
}
