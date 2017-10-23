package com.example.a731.aclass.activity;

import android.content.Intent;
import android.os.Handler;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.SplashPresenter;
import com.example.a731.aclass.presenter.impl.SplashPresenterImpl;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.view.SplashView;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by 郑选辉 on 2017/9/19.
 */

public class SplashActivity extends BaseActivity implements SplashView{

    private SplashPresenter splashPresenter = new SplashPresenterImpl(this);

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EaseMobUtil.onLoad();
                splashPresenter.checkLoginStatus();
            }
        },3000);

    }

    @Override
    public void onLoggedIn() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onNotLogin() {
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
