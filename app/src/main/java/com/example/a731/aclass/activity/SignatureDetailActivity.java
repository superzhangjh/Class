package com.example.a731.aclass.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.SignatureDetailAdapter;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.SignatureDetailPresenter;
import com.example.a731.aclass.presenter.impl.SignatureDetailPresenterImpl;
import com.example.a731.aclass.view.SignatureDetailView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郑选辉 on 2017/11/7.
 */

public class SignatureDetailActivity extends BaseActivity implements SignatureDetailView{

    private Toolbar mToolbar;
    private TextView tvTime,tvName;
    private RecyclerView recyclerView;
    private SignatureDetailPresenter presenter = new SignatureDetailPresenterImpl(this);
    private SignatureDetailAdapter adapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_signature_detail;
    }

    @Override
    public void initView() {
        initToolBar();
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.signature_detail_recycler_content);
        adapter = new SignatureDetailAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void initToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.signature_detail_toolbar);
        mToolbar.setTitle("");
        mToolbar.setTitleTextColor(Color.RED);
        setSupportActionBar(mToolbar);

        // 显示标题栏左上角的返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //点击toolbar后导航栏 左上角的图标后，退出当前界面
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String time = intent.getStringExtra("time");

        tvName = (TextView) findViewById(R.id.signature_detail_tv_classname);
        tvName.setText(name);
        tvTime = (TextView) findViewById(R.id.signature_detail_tv_time);
        tvTime.setText(time);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        String objectId = intent.getStringExtra("objectId");
        String groupId = intent.getStringExtra("groupObjectId");
        presenter.getSignatureDetail(objectId,groupId);
    }

    @Override
    public void onGetSignatureMemberSuccess(List<Users> groupMembers, List<Users> signMembers) {
        List<String> signMembersID = new ArrayList<>();
        for (Users users:signMembers){
            signMembersID.add(users.getUsername());
            Log.i("SignatureDetail",users.getUsername());
        }
        adapter.setListData(groupMembers,signMembersID);
    }

    @Override
    public void onGetSignatureMemberFail(String message) {
        showToast("获取数据失败："+message);
    }
}
