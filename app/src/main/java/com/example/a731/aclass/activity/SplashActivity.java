package com.example.a731.aclass.activity;

import android.content.Intent;
import android.os.Handler;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Users;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by 郑选辉 on 2017/9/19.
 */

public class SplashActivity extends BaseActivity {
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
        //Bmob.initialize(this, "220e091cec75d58301d05e224f0c3af0");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                Users user = BmobUser.getCurrentUser(Users.class);
                if (user!=null){
                    intent.setClass(SplashActivity.this,MainActivity.class);
                }else{
                    intent.setClass(SplashActivity.this,LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        },3000);

    }
}
