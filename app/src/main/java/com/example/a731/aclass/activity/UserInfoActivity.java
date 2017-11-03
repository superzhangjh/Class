package com.example.a731.aclass.activity;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.UserInfoPresenter;
import com.example.a731.aclass.presenter.impl.UserInfoPresenterImpl;
import com.example.a731.aclass.view.UserInfoView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/10/3/003.
 */

public class UserInfoActivity extends BaseActivity implements UserInfoView {

    private Toolbar toolbar;
    private CircleImageView imgHead;//头像
    private ImageView imgHeadBG;//头像背景的点击事件(本地功能)
    private Users user;//用户数据从网络获取，便于修改资料的更新

    private String username;

    private ImageView ivSex;//性别图标
    private ImageView itQRCode;//二维码的点击事件
    private TextView tvName;//名字
    private TextView tvUsername;//用户名
    private TextView tvPhoneNember;//;电话号码
    private TextView tvId;//学号
    private TextView tvProject;//专业
    private TextView tvHomeland;//家乡
    private TextView tvIntro;//简介

    private UserInfoPresenter userInfoPresenter = new UserInfoPresenterImpl(this);

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_userinfo;
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        toolbar = (Toolbar) findViewById(R.id.userinfo_toolbar);
        imgHead = (CircleImageView) findViewById(R.id.userinfo_iv_head);
        imgHeadBG = (ImageView) findViewById(R.id.userinfo_headbackground);

        ivSex = (ImageView) findViewById(R.id.userinfo_iv_sex);
        itQRCode = (ImageView) findViewById(R.id.userinfo_iv_qr_droid);
        tvName = (TextView) findViewById(R.id.userinfo_tv_name);
        tvUsername = (TextView) findViewById(R.id.userinfo_tv_username);
        tvPhoneNember = (TextView) findViewById(R.id.userinfo_tv_phonenumber);
        tvProject = (TextView) findViewById(R.id.userinfo_tv_project);
        tvHomeland = (TextView) findViewById(R.id.userinfo_tv_homeland);
        tvIntro = (TextView) findViewById(R.id.userinfo_tv_intro);
        initToolbar();
    }

    private void initBinderData() {

        //读取User的属性
        int sex = user.getSex();
        String name = user.getName();
        String username = user.getUsername();
        String phoneNumber = user.getMobilePhoneNumber();
        String project =  user.getProject();
        String hoemland = user.getHomeLand();
        String intro = user.getIntro();
        String qrCode = user.getQRCode();


        //性别图标显示
        if (sex==1){
            ivSex.setBackgroundResource(R.drawable.male);
        } else if (sex ==2){
            ivSex.setBackgroundResource(R.drawable.female);
        }

        //数据与UI控件的绑定
        tvName.setText(name);
        tvUsername.setText(username);
        tvPhoneNember.setText(phoneNumber);
        //tvIntro.setText(Id);
        tvProject.setText(project);
        tvHomeland.setText(hoemland);
        tvIntro.setText(intro);
    }

    private void initToolbar() {
        //设置toolbar
        setSupportActionBar(toolbar);
        toolbar.setAlpha(0.8f);
        // 显示标题栏左上角的返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //点击toolbar后导航栏 左上角的图标后，退出当前界面
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.userinfo_toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void initListener() {
        itQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),QRCodeActivity.class);
                intent.putExtra("headImage",user.getHeadImg());
                intent.putExtra("qrCode",user.getQRCode());
                intent.putExtra("name",user.getName());
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        userInfoPresenter.getUser(username);
    }

    @Override
    public void onGetUserSuccess(Users users) {
        user = users;
        initBinderData();
    }

    @Override
    public void onGetUserFail(String message) {
        showToast("获取用户信息失败"+message);
    }
}
