package com.example.a731.aclass.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.AddFriendPresenter;
import com.example.a731.aclass.presenter.QRCodeResultGroupPresenter;
import com.example.a731.aclass.presenter.impl.AddFriendPresenterImpl;
import com.example.a731.aclass.presenter.impl.QRCodeResultGroupPresenterImpl;
import com.example.a731.aclass.view.AddFriendView;
import com.example.a731.aclass.view.QRCodeResultGroupView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/11/4/004.
 */

public class QRCodeRusultGroupActivity extends BaseActivity implements QRCodeResultGroupView{

    private Toolbar toolbar;
    private CircleImageView ivClassHead;
    private TextView tvSize;
    private TextView tvClassName;
    private CircleImageView tvCreatorHead;
    private TextView tvCreatorName;
    private TextView tvIntro;
    private Button btnJoin;
    private Users users;
    private Group group;

    private QRCodeResultGroupPresenter presenter = new QRCodeResultGroupPresenterImpl(this);

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_qr_code_result_group;
    }

    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.activity_result_group_toolbar);
        ivClassHead = (CircleImageView) findViewById(R.id.activity_result_group_iv_class_head);
        tvSize = (TextView) findViewById(R.id.activity_result_group_tv_size);
        tvClassName = (TextView) findViewById(R.id.activity_result_group_tv_class_name);
        tvCreatorHead = (CircleImageView) findViewById(R.id.activity_result_group_iv_creator_head);
        tvCreatorName = (TextView) findViewById(R.id.activity_result_group_tv_creator_name);
        tvIntro = (TextView) findViewById(R.id.activity_result_group_tv_intro);
        btnJoin = (Button) findViewById(R.id.activity_result_group_btn_join);
        initToolbar();
        initData();
    }

    @Override
    public void initData() {
        String groupId = getIntent().getStringExtra("groupId");
        //根据groupId获取班级信息
        presenter.getGroup(groupId);
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
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.joinGroup(group.getGroupId(),group.getObjectId());
            }
        });
    }


    //获取网络数据成功时设置内容
    private void setView() {
        Glide.with(getApplicationContext()).load(group.getHeadImg()).into(ivClassHead);//班圈头像
        //tvSize.setText();成员数量
        tvClassName = (TextView) findViewById(R.id.activity_result_group_tv_class_name);
        tvClassName.setText(group.getName());
        Glide.with(getApplicationContext()).load(users.getHeadImg()).into(tvCreatorHead);
        tvCreatorName.setText(users.getName());
        //tvIntro.setText(group.);班圈简介
    }

    @Override
    public void onGetGroupSuccess(Group group) {
        this.group = group;
        users = group.getCreator();
        setView();
    }

    @Override
    public void onGetGroupFail(String message) {
        showToast("获取班圈信息失败："+message);
    }

    @Override
    public void onJoinGroupSuccess() {
        showToast("申请加入班圈成功");
        finish();
    }

    @Override
    public void onJoinGroupFail(String message) {
        showToast("申请加入班圈失败："+message);
    }
}
