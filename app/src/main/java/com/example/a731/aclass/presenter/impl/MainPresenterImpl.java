package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.presenter.MainPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.view.MainView;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;

/**
 * Created by 郑选辉 on 2017/9/27.
 */

public class MainPresenterImpl implements MainPresenter {

    private MainView mMainView;

    public MainPresenterImpl(MainView mainView){
        mMainView = mainView;
    }

    public void logOut(){
        BmobUtil.logOut();
        EaseMobUtil.logout(new EMCallBack() {
            @Override
            public void onSuccess() {
                mMainView.onLogoutSuccess();
            }

            @Override
            public void onError(int code, String error) {
                mMainView.onLogoutFail(error);
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    @Override
    public void checkConnectionState() {
        EaseMobUtil.onConnectingListener(new EMConnectionListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(int errorCode) {
                mMainView.onDisconnected(errorCode);
            }
        });
    }


}
