package com.example.a731.aclass.fragment;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.NoticeAdapter;
import com.example.a731.aclass.activity.ReleasingNoticesActivity;
import com.example.a731.aclass.data.Notice;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/10/6/006.
 */

public class CircleNoticeFragment extends BaseFragment{
    private static final int REQUEST_RELEASING_NOTICE = 1001;
    private RecyclerView mRecyclerView;
    //private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Notice> noticeList = new ArrayList<>();
    private NoticeAdapter adapter;
    private CardView Btn1;


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_circle_notice;
    }

    @Override
    public void initView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.circle_base_recyclerview);
        //mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.circle_base_swiperefresh);
        Btn1 = (CardView) mRootView.findViewById(R.id.notice_head_1);
        Btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:判断该用户的ID是否在该班圈的管理员列表中，如果存在则跳转到下面的活动，如果不存在则弹出提示没有该权限
                Intent intent = new Intent(getContext(), ReleasingNoticesActivity.class);
                startActivityForResult(intent,REQUEST_RELEASING_NOTICE);
            }
        });

        adapter = new NoticeAdapter(getContext(),noticeList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_RELEASING_NOTICE :
                if (resultCode==RESULT_OK){
                    String content = data.getStringExtra("content");
                    String date = data.getStringExtra("date");

                    Notice notice = new Notice();
                    notice.setContent(content);
                    notice.setDate(date);
                    noticeList.add(notice);

                    adapter.setListData(noticeList);
                }
        }
    }
}
