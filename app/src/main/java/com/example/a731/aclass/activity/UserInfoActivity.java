package com.example.a731.aclass.activity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Users;

/**
 * Created by Administrator on 2017/10/3/003.
 */

public class UserInfoActivity extends BaseActivity{

    private Toolbar toolbar;
    private ImageView imgHead;//头像
    private ImageView imgHeadBG;//头像背景的点击事件(本地功能)
    private Users user;//用户数据从网络获取，便于修改资料的更新

    private ImageView ivSex;//性别图标
    private ImageView itQRCode;//二维码的点击事件
    private TextView tvName;//名字
    private TextView tvUsername;//用户名
    private TextView tvPhoneNember;//;电话号码
    private TextView tvId;//学号
    private TextView tvProject;//专业
    private TextView tvHomeland;//家乡
    private TextView tvIntro;//简介

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_userinfo;
    }

    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.userinfo_toolbar);
        imgHead = (ImageView) findViewById(R.id.userinfo_iv_head);
        imgHeadBG = (ImageView) findViewById(R.id.userinfo_headbackground);

        ivSex = (ImageView) findViewById(R.id.userinfo_iv_sex);
        itQRCode = (ImageView) findViewById(R.id.userinfo_iv_qr_droid);
        tvName = (TextView) findViewById(R.id.userinfo_tv_name);
        tvUsername = (TextView) findViewById(R.id.userinfo_tv_username);
        tvPhoneNember = (TextView) findViewById(R.id.userinfo_tv_phonenumber);
        tvId = (TextView) findViewById(R.id.userinfo_tv_id);
        tvProject = (TextView) findViewById(R.id.userinfo_tv_project);
        tvHomeland = (TextView) findViewById(R.id.userinfo_tv_homeland);
        tvIntro = (TextView) findViewById(R.id.userinfo_tv_intro);
        initToolbar();
        initBinderData();
    }

    private void initBinderData() {
        //获取网络数据(待实现)
        user = new Users();
        user.setSex(2);
        user.setName("张三");
        user.setUsername("zhangsan");
        user.setMobilePhoneNumber("15625015545");


        //读取User的属性
        int sex = user.getSex();
        String name = user.getName();
        String username = user.getUsername();
        String phoneNumber = user.getMobilePhoneNumber();
        int Id = user.getId();
        String project =  user.getProject();
        String hoemland = user.getHomeLand();
        String intro = user.getIntro();

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

    }

    @Override
    public void initData() {
    }
}
