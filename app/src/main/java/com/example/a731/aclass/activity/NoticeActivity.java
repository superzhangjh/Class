package com.example.a731.aclass.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.PhotoAdapter;
import com.example.a731.aclass.data.Notice;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 郑选辉 on 2017/10/26.
 */

public class NoticeActivity extends BaseActivity {
    private Toolbar toolbar;
    private CircleImageView ivHead;
    private TextView tvCreatorName;
    private TextView tvTitle;
    private TextView tvDate;
    private TextView tvGroupName;
    private TextView tvContent;
    private RecyclerView recyclePhoto;

    private Notice notice;

    @Override
    protected int getLayoutRes() {
        return R.layout.acticity_notice;
    }

    @Override
    public void initView() {
        findViews();
        initData();
        initToolbal();

        if (notice!=null){
            Glide.with(this).load(notice.getCreator().getHeadImg()).into(ivHead);
            tvCreatorName.setText(notice.getCreator().getName());
            tvGroupName.setText(notice.getGroup().getName());

            tvTitle.setText(notice.getTitle());
            tvDate.setText(notice.getDate());
            tvContent.setText(notice.getContent());
            PhotoAdapter photoAdapter = new PhotoAdapter(this,notice.getPhotoList());
            recyclePhoto.setLayoutManager(new GridLayoutManager(this,3));
            recyclePhoto.setAdapter(photoAdapter);
        }
    }

    private void initToolbal() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
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

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.activity_notice_toolbar);
        ivHead = (CircleImageView) findViewById(R.id.activity_notice_iv_head);
        tvCreatorName = (TextView) findViewById(R.id.activity_notice_tv_creator_name);
        tvTitle = (TextView) findViewById(R.id.activity_notice_tv_title);
        tvDate = (TextView) findViewById(R.id.activity_notice_tv_date);
        tvGroupName = (TextView) findViewById(R.id.activity_notice_tv_group_name);
        tvContent = (TextView) findViewById(R.id.activity_notice_tv_content);
        recyclePhoto = (RecyclerView) findViewById(R.id.activity_notice_recycle_photo);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        notice =(Notice)intent.getSerializableExtra("notice");
    }
}
