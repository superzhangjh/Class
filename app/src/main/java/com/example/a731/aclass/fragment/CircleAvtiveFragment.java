package com.example.a731.aclass.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.example.a731.aclass.R;

/**
 * Created by Administrator on 2017/10/6/006.
 */

public class CircleAvtiveFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_circle_active;
    }

    @Override
    public void initView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.circleactive_recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.circleactive_swiperefresh);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }
}
