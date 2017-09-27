package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.presenter.MainPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.view.MainView;

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
    }
}
