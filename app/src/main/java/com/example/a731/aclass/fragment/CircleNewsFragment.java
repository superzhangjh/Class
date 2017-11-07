package com.example.a731.aclass.fragment;

import android.content.Intent;
import android.os.Handler;
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
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/10/6/006.
 */

public class CircleNewsFragment extends BaseFragment implements CircleNewsView {
    private RecyclerView mRecyclerView;
    private List<News> newsList;
    private SpringView springView;
    private NewsAdapter adapter;

    private String presentGroupId;
    private CircleNewsPresenter presenter = new CircleNewsPresenterImpl(this);


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_circle_base;
    }

    @Override
    public void initView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.circle_base_recyclerview);
        initRecyclerView();
    }

    @Override
    protected void initSpringView() {
        springView = (SpringView) mRootView.findViewById(R.id.circle_base_spring_view);
        springView.setFooter(new AliFooter(getContext()));
        springView.setHeader(new AliHeader(getContext()));
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presentGroupId = SharedPreferencesUtil.lodaDataFromSharedPreferences(BmobUser.getCurrentUser().getUsername(),getContext());
                        if (presentGroupId!=null)
                            presenter.getGroupNews(presentGroupId);
                        springView.onFinishFreshAndLoad();
                    }
                }, 2000);
            }
            @Override
            public void onLoadmore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        springView.onFinishFreshAndLoad();
                    }
                }, 2000);            }
        });
    }

    private void initRecyclerView() {
        adapter = new NewsAdapter(getContext(), newsList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemViewCacheSize(20);
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
        //showToast("获取动态成功"+list.size());
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

    @Override
    public void onResume() {
        super.onResume();
        presentGroupId = SharedPreferencesUtil.lodaDataFromSharedPreferences(BmobUser.getCurrentUser().getUsername(),getContext());
        if (presentGroupId!=null)
            presenter.getGroupNews(presentGroupId);
    }
}
