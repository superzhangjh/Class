package com.example.a731.aclass.fragment;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.NoticeAdapter;
import com.example.a731.aclass.activity.ReleasingNoticesActivity;
import com.example.a731.aclass.data.Notice;
import com.example.a731.aclass.presenter.CircleNoticePresenter;
import com.example.a731.aclass.presenter.impl.CircleNoticePresenterImpl;
import com.example.a731.aclass.util.SharedPreferencesUtil;
import com.example.a731.aclass.view.CircleNoticeView;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/10/6/006.
 */

public class CircleNoticeFragment extends BaseFragment implements CircleNoticeView {
    private static final int REQUEST_RELEASING_NOTICE = 1001;
    private RecyclerView mRecyclerView;
    //private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Notice> noticeList = new ArrayList<>();
    private NoticeAdapter adapter;
    private LinearLayout btn1;

    private String presentGroupId;

    private CircleNoticePresenter presenter = new CircleNoticePresenterImpl(this);



    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_circle_notice;
    }

    @Override
    public void initView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.circle_base_recyclerview);
        //mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.circle_base_swiperefresh);
        btn1 = (LinearLayout) mRootView.findViewById(R.id.notice_head_1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (presenter.queryAdmin()){
                Intent intent = new Intent(getContext(), ReleasingNoticesActivity.class);
                startActivityForResult(intent,REQUEST_RELEASING_NOTICE);
                //}else{
                showToast("你不是管理员,没有该权限");
                //}
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
        presentGroupId = SharedPreferencesUtil.lodaDataFromSharedPreferences("groupId",getContext());
        presenter.getGroupNotice(presentGroupId);
    }

    @Override
    public void onGetGroupFail(String message) {
        showToast("获取班圈信息失败:"+message);
    }

    @Override
    public void onGetAdminFail(String message) {
        showToast("获取管理员列表失败:"+message);
    }

    @Override
    public void onGetNoticeSuccess(List<Notice> list) {
        adapter.setListData(list);
        showToast("获取通知列表成功"+list.size()+"--"+presentGroupId);
    }

    @Override
    public void onGetNoticeFail(String message) {
        showToast("获取通知列表失败:"+message);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getGroupNotice(presentGroupId);
    }

}
