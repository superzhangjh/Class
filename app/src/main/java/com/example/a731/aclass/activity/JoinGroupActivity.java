package com.example.a731.aclass.activity;

import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.presenter.JoinGroupPresenter;
import com.example.a731.aclass.presenter.impl.JoinGroupPresenterImpl;
import com.example.a731.aclass.view.JoinGroupView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 郑选辉 on 2017/10/18.
 */

public class JoinGroupActivity extends BaseActivity implements JoinGroupView{

    private CircleImageView headImg;
    private EditText edtGroupId;
    private TextView tvName,tvCreator;
    private Button btnSearch,btnJoin;
    private LinearLayout content;

    private final String TAG = "JoinGroupActivity";

    private Group mGroup;

    private JoinGroupPresenter joinGroupPresenter = new JoinGroupPresenterImpl(this);
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_joingroup;
    }

    @Override
    public void initView() {
        edtGroupId = (EditText) findViewById(R.id.join_group_edt_groupId);
        headImg = (CircleImageView) findViewById(R.id.join_group_iv_headImg);
        tvName = (TextView) findViewById(R.id.join_group_tv_name);
        tvCreator = (TextView) findViewById(R.id.join_group_tv_creator);
        btnSearch = (Button) findViewById(R.id.join_group_btn_search);
        btnJoin = (Button) findViewById(R.id.join_group_btn_join);
        content = (LinearLayout) findViewById(R.id.join_group_ll_content);


    }

    @Override
    public void initListener() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupId = edtGroupId.getText().toString();
                joinGroupPresenter.searchGroup(groupId);
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"点击事件，开始加入班圈");
                //showProgress("正在加入班圈");
                joinGroupPresenter.joinGroup();
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onSearchGroupSuccess(Group group) {
        mGroup = group;
        Glide.with(this).load(mGroup.getHeadImg()).into(headImg);
        tvName.setText(mGroup.getName());
        tvCreator.setText(mGroup.getCreator().getUsername());
        content.setVisibility(View.VISIBLE);
        showToast("获取班圈成功");
    }

    @Override
    public void onSearchGroupFail(String message) {
        showToast("获取班圈失败"+message);
    }

    @Override
    public void onJoinGroupSuccess() {
        showToast("加入班圈成功");
        //hideProgress();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onJoinGroupFail(String message) {
        //hideProgress();
        showToast("加入班圈失败"+message);
    }
}
