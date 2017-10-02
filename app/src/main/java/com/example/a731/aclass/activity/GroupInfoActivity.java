package com.example.a731.aclass.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a731.aclass.R;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2017/10/2/002.
 */

public class GroupInfoActivity extends BaseActivity{

    private Toolbar toolbar;
    private LinearLayout tvQRCode;
    private LinearLayout tvNickname;
    private LinearLayout llMemberList;
    private TextView tvRecommend;
    private RecyclerView recyclerView;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_groupinfo;
    }

    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.groupinfo_toolbar);
        tvQRCode = (LinearLayout) findViewById(R.id.groupinfo_tv_qrcode);
        tvNickname = (LinearLayout) findViewById(R.id.groupinfo_ll_nickname);
        llMemberList = (LinearLayout) findViewById(R.id.groupinfo_ll_memberlist);
        tvRecommend = (TextView) findViewById(R.id.groupinfo_tv_recommend);
        recyclerView = (RecyclerView) findViewById(R.id.groupinfo_recycle_headlist);
        
        initToolbar();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        // 显示标题栏左上角的返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // 导航栏图标显示
        //toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        //点击toolbar后导航栏 左上角的图标后，退出当前界面
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.groupinfo_toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
