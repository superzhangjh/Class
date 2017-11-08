package com.example.a731.aclass.activity;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.fragment.CircleFragment;
import com.example.a731.aclass.fragment.CircleNewsFragment;
import com.example.a731.aclass.fragment.MessFragment;
import com.example.a731.aclass.fragment.UserCommendFragment;
import com.example.a731.aclass.fragment.UserInfoFragment;
import com.example.a731.aclass.presenter.UserInfoPresenter;
import com.example.a731.aclass.presenter.impl.UserInfoPresenterImpl;
import com.example.a731.aclass.util.customView.NoScrollViewPager;
import com.example.a731.aclass.view.UserInfoView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/10/3/003.
 */

public class UserInfoActivity extends BaseActivity implements UserInfoView {

    private Toolbar toolbar;
    private ImageView imgHead;//头像
    private Users user;//用户数据从网络获取，便于修改资料的更新
    private RadioButton btnInfo;
    private RadioButton btnNews;
    private RadioButton btnCommend;
    private NoScrollViewPager viewPager;
    private RadioGroup rbNavGroup;

    private String username;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;

    private UserInfoPresenter userInfoPresenter = new UserInfoPresenterImpl(this);

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_userinfo;
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        /* ---------------------   调用该activity需要传入用户的username----------------------------------*/
        username = intent.getStringExtra("username");
        showProgress("获取个人资料");
        userInfoPresenter.getUser(username);

        rbNavGroup = (RadioGroup) findViewById(R.id.uesrinfo_radio_group);
        btnInfo = (RadioButton) findViewById(R.id.userinfo_btn_info);
        btnNews = (RadioButton) findViewById(R.id.userinfo_btn_news);
        btnCommend = (RadioButton) findViewById(R.id.userinfo_btn_commend);
        viewPager = (NoScrollViewPager) findViewById(R.id.userinfo_view_pager);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.userinfo_collapsing_toolbar);
        toolbar = (Toolbar) findViewById(R.id.userinfo_toolbar);
        imgHead = (ImageView) findViewById(R.id.userinfo_iv_head);
        appBarLayout = (AppBarLayout) findViewById(R.id.userinfo_appBar);
        initToolbar();
        initViewPager();
    }

    private void initBinderData() {
        //设置头像
        Glide.with(this).load(user.getHeadImg()).crossFade().placeholder(R.drawable.default_head_image).error(R.drawable.cross86).into(imgHead);
        collapsingToolbarLayout.setCollapsedTitleTextColor(0x00000000);
    }

    private void initToolbar() {
        //设置toolbar
        toolbar.setTitle("个人主页");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        // 显示标题栏左上角的返回按钮
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        collapsingToolbarLayout.setTitleEnabled(false);
    }

    private void initViewPager() {
        viewPager.setCurrentItem(3);
        final List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new UserInfoFragment(username));
        fragmentList.add(new CircleNewsFragment());
        fragmentList.add(new UserCommendFragment());

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()){
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }
            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_userinfo_toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void initListener() {
        rbNavGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.userinfo_btn_info:
                        viewPager.setCurrentItem(0);
                        btnInfo.setTextColor(0xFF5E5E5E);
                        btnNews.setTextColor(0xFFAAAAAA);
                        btnCommend.setTextColor(0xFFAAAAAA);
                        break;
                    case R.id.userinfo_btn_news:
                        viewPager.setCurrentItem(1);
                        btnInfo.setTextColor(0xFFAAAAAA);
                        btnNews.setTextColor(0xFF5E5E5E);
                        btnCommend.setTextColor(0xFFAAAAAA);
                        break;
                    case R.id.userinfo_btn_commend:
                        viewPager.setCurrentItem(2);
                        btnInfo.setTextColor(0xFFAAAAAA);
                        btnNews.setTextColor(0xFFAAAAAA);
                        btnCommend.setTextColor(0xFF5E5E5E);
                        break;
                }
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        btnInfo.setTextColor(0xFF5E5E5E);
                        btnNews.setTextColor(0xFFAAAAAA);
                        btnCommend.setTextColor(0xFFAAAAAA);
                        break;
                    case 1:
                        btnInfo.setTextColor(0xFFAAAAAA);
                        btnNews.setTextColor(0xFF5E5E5E);
                        btnCommend.setTextColor(0xFFAAAAAA);
                        break;
                    case 2:
                        viewPager.setCurrentItem(2);
                        btnInfo.setTextColor(0xFFAAAAAA);
                        btnNews.setTextColor(0xFFAAAAAA);
                        btnCommend.setTextColor(0xFF5E5E5E);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initData() {
    }

    @Override
    public void onGetUserSuccess(Users users) {
        user = users;
        initBinderData();
        hideProgress();
    }

    @Override
    public void onGetUserFail(String message) {
        showToast("获取用户信息失败"+message);
        hideProgress();
    }
}
