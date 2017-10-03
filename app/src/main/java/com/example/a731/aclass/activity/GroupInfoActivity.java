package com.example.a731.aclass.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.GroupInfoMemberAdapter;
import com.example.a731.aclass.data.Users;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.example.a731.aclass.R.id.groupinfo_recyclerview_memberlist;
import static com.example.a731.aclass.R.id.list_item;

/**
 * Created by Administrator on 2017/10/2/002.
 */

public class GroupInfoActivity extends BaseActivity{

    private Toolbar toolbar;
    private LinearLayout tvQRCode;
    private LinearLayout tvNickname;
    private LinearLayout llMemberList;
    private TextView tvRecommend;
    private RecyclerView memberRecyclerView;
    private ImageView memIcon;

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
        memIcon = (ImageView) findViewById(R.id.groupinfo_iv_membericon);

        initToolbar();
        initAddView();

    }

    private void initGalleryRecycleView() {
        List<Users> memberlist = new ArrayList<>();
        for (int i=0;i<20;i++){
            Users user = new Users();
            user.setId(i);
            user.setName("学生"+i);
            memberlist.add(user);
        }
        memberRecyclerView = (RecyclerView) findViewById(groupinfo_recyclerview_memberlist);
        //设置表格布局
        GroupInfoMemberAdapter memberAdapter = new GroupInfoMemberAdapter(getApplicationContext(),memberlist);
        GridLayoutManager layoutManage = new GridLayoutManager(getApplicationContext(), 5){
            //设置不可滑动
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        memberRecyclerView.setLayoutManager(layoutManage);
        memberRecyclerView.setAdapter(memberAdapter);

    }

    private void initAddView() {
        //点击添加recyclerview,右侧图标旋转
        final View subView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_groupinfo_sub_memberlist,null,false);
        LinearLayout.LayoutParams subParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,200);
        final boolean[] isOpen = {false};
        llMemberList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpen[0]){
                    llMemberList.addView(subView);
                    initGalleryRecycleView();
                    //图标旋转
                    memIcon.animate().rotation(-180);
                }else {
                    llMemberList.removeView(subView);
                    //图标旋转
                    memIcon.animate().rotation(0);
                }
                isOpen[0] = !isOpen[0];
            }
        });
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

    private void initRecycleView() {

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
