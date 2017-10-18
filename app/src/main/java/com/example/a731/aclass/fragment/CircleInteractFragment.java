package com.example.a731.aclass.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.a731.aclass.R;
import com.example.a731.aclass.activity.ReleasingNoticesActivity;
import com.example.a731.aclass.adapter.InteractAdapter;
import com.example.a731.aclass.adapter.NoticeAdapter;
import com.example.a731.aclass.data.Notice;
import com.example.a731.aclass.data.Vote;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/10/6/006.
 */

public class CircleInteractFragment extends BaseFragment{
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Vote> voteList = new ArrayList<>();
    private InteractAdapter adapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_circle_base;
    }

    @Override
    public void initView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.circle_base_recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.circle_base_swiperefresh);

        //adapter = new InteractAdapter(getContext(),voteList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //RecyclerView.setAdapter(adapter);
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
    }
}
