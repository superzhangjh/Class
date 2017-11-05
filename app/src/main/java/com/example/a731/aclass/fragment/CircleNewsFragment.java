package com.example.a731.aclass.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.activity.ReleasingNewsActivity;
import com.example.a731.aclass.adapter.NewsAdapter;
import com.example.a731.aclass.data.News;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.CircleNewsPresenter;
import com.example.a731.aclass.presenter.impl.CircleNewsPresenterImpl;
import com.example.a731.aclass.util.DateUtil;
import com.example.a731.aclass.util.SharedPreferencesUtil;
import com.example.a731.aclass.view.CircleNewsView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/10/6/006.
 */

public class CircleNewsFragment extends BaseFragment implements CircleNewsView{
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<News> newsList;
    private NewsAdapter adapter;

    private String presentGroupId;
    private CircleNewsPresenter presenter = new CircleNewsPresenterImpl(this);

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_circle_news;
    }

    @Override
    public void initView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.circle_news_recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.circle_news_swiperefresh);
        initRecyclerView();
    }



    private void initRecyclerView() {
        adapter = new NewsAdapter(getContext(), newsList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemViewCacheSize(10);
        adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showToast(position+"");
            }
        });
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        presentGroupId = SharedPreferencesUtil.lodaDataFromSharedPreferences(BmobUser.getCurrentUser().getUsername(),getContext());
        if (presentGroupId!=null)
            presenter.getGroupNews(presentGroupId);
    }

    @Override
    public void onGetNewsSuccess(List<News> list) {
        showToast("获取动态成功"+list.size());
        newsList = list;
        if (adapter != null){
            adapter.setListData(newsList);
        }
    }

    @Override
    public void onGetNewsFail(String message) {

    }

    @Override
    public void onGetGroupFail(String message) {

    }
}
