package com.example.a731.aclass.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.AddFriendPresenter;
import com.example.a731.aclass.presenter.impl.AddFriendPresenterImpl;
import com.example.a731.aclass.view.AddFriendView;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/11/4/004.
 */

public class QRCodeRusultUserActivity extends BaseActivity implements AddFriendView{

    private Toolbar toolbar;
    private CircleImageView ivHead;
    private TextView tvName;
    private ImageView ivSex;
    private TextView tvClassName;
    private TextView tvIntro;
    private Button btnAdd;

    private Users users;

    private AddFriendPresenter presenter = new AddFriendPresenterImpl(this);

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_qr_code_result_friend;
    }

    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.activity_result_user_toolbar);
        ivHead = (CircleImageView) findViewById(R.id.activity_result_user_iv_head);
        tvName = (TextView) findViewById(R.id.activity_result_user_tv_name);
        ivSex = (ImageView) findViewById(R.id.activity_result_user_iv_sex);
        tvClassName = (TextView) findViewById(R.id.activity_result_user_tv_class_name);
        tvIntro = (TextView) findViewById(R.id.activity_result_user_tv_intro);
        btnAdd = (Button) findViewById(R.id.activity_result_user_btn_add);
        
        initToolbar();
        initData();
    }

    @Override
    public void initData() {
        //从上个页面获取传递的username数据
        String username = getIntent().getStringExtra("username");
        showProgress("正在获取信息");
        presenter.getUser(username);
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initListener() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.addFriend(users.getUsername());
            }
        });
    }

    @Override
    public void onGetUserSuccess(Users users) {
        this.users = users;
        hideProgress();
        setView();
    }

    //获取网络数据成功时设置内容
    private void setView() {
        //设置内容
        Glide.with(getApplicationContext()).load(users.getHeadImg()).into(ivHead);
        tvName.setText(users.getName());
        int sex = users.getSex();
        //性别图标显示
        if (sex==1){
            ivSex.setBackgroundResource(R.drawable.male);
        } else if (sex==2){
            ivSex.setBackgroundResource(R.drawable.female);
        }
        tvClassName.setText(users.getProject());
        tvIntro.setText(users.getIntro());
    }

    @Override
    public void onGetUserFail(String message) {
        this.users = new Users();
        showToast("获取用户失败:"+message);
        hideProgress();
    }

    @Override
    public void onAddFriendSuccess() {
        showToast("申请好友成功");
        finish();
    }

    @Override
    public void onAddFriendFail(String message) {
        showToast("申请好友失败:"+message);
    }
}
