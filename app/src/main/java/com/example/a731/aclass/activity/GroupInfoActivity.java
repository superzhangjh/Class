package com.example.a731.aclass.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
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

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.GroupInfoMemberAdapter;
import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.GroupInfoPresenter;
import com.example.a731.aclass.presenter.impl.GroupInfoPresenterImpl;
import com.example.a731.aclass.view.GroupInfoView;
import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.a731.aclass.R.id.groupinfo_recyclerview_memberlist;

/**
 * Created by Administrator on 2017/10/2/002.
 */

public class GroupInfoActivity extends BaseActivity implements GroupInfoView{

    private Toolbar toolbar;
    private LinearLayout tvQRCode;
    private LinearLayout tvNickname;
    private LinearLayout llMemberList;
    private TextView tvRecommend,tvMemberCount,tvGroupName,tvGroupId;
    private RecyclerView memberRecyclerView;
    private ImageView memIcon;
    private CircleImageView ivGroupHead;
    private GroupInfoMemberAdapter memberAdapter;
    private List<Users> memberList;


    private int ADD_MORE_MEMBER = 1001;


    private static final String TAG="GroupInfoActivity";
    private GroupInfoPresenter groupInfoPresenter = new GroupInfoPresenterImpl(this);

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_groupinfo;
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        String groupId = intent.getStringExtra("groupId");
        toolbar = (Toolbar) findViewById(R.id.groupinfo_toolbar);
        tvQRCode = (LinearLayout) findViewById(R.id.groupinfo_tv_qrcode);
        tvNickname = (LinearLayout) findViewById(R.id.groupinfo_ll_nickname);
        llMemberList = (LinearLayout) findViewById(R.id.groupinfo_ll_memberlist);
        tvRecommend = (TextView) findViewById(R.id.groupinfo_tv_recommend);
        tvMemberCount = (TextView) findViewById(R.id.groupinfo_tv_membercount);
        tvGroupName = (TextView) findViewById(R.id.groupinfo_toolbar_tv_name);
        tvGroupId = (TextView) findViewById(R.id.groupinfo_toolbar_tv_id);
        memIcon = (ImageView) findViewById(R.id.groupinfo_iv_membericon);
        ivGroupHead = (CircleImageView) findViewById(R.id.groupinfo_toolbar_iv_head);



        initToolbar();
        initAddView();

        showProgress("正在加载圈界面");

        groupInfoPresenter.getGroup(groupId);


    }

    private void initGalleryRecycleView() {

        memberRecyclerView = (RecyclerView) findViewById(groupinfo_recyclerview_memberlist);
        //设置表格布局
        memberAdapter = new GroupInfoMemberAdapter(getApplicationContext(),memberList);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.groupinfo_toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGetGroupMemberFail(String message) {
        showToast("获取群成员失败"+message);
    }

    @Override
    public void onGetGroupMemberSuccess(List<Users> list) {
        showToast("获取群成员成功"+list.size());
        tvMemberCount.setText(list.size()+"");
        Users user = new Users();
        user.setName("邀请成员");
        user.setId(ADD_MORE_MEMBER);
        //user.setHeadImg(R.drawable.chat_fn);
        list.add(user);
        hideProgress();
        if (memberAdapter!=null){
            memberAdapter.onDataChange(list);
        }else{
            memberList = list;
        }

    }

    @Override
    public void onGetGroupSuccess(List<Group> list) {
        Group group = list.get(0);
        groupInfoPresenter.getGroupMember(group.getObjectId());
        tvRecommend.setText("本圈创建于"+group.getCreatedAt());
        tvGroupName.setText(group.getName());
        tvGroupId.setText("班圈ID:"+group.getGroupId());
        Glide.with(this).load(group.getHeadImg()).into(ivGroupHead);
    }

    @Override
    public void onGetGroupFail(String s) {
        showToast("获取班圈失败"+s);
    }
}
