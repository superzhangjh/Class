package com.example.a731.aclass.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.CommendAdapter;
import com.example.a731.aclass.data.Commend;
import com.liaoinstan.springview.widget.SpringView;

import java.util.List;

/**
 * Created by Administrator on 2017/11/7/007.
 */

public class UserCommendFragment extends BaseFragment {
    private SpringView springView;
    private RecyclerView recyclerview;

   private List<Commend> commendList;


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_circle_base;
    }

    @Override
    public void initView() {
        springView = (SpringView) mRootView.findViewById(R.id.circle_base_spring_view);
        recyclerview = (RecyclerView) mRootView.findViewById(R.id.circle_base_recyclerview);

        CommendAdapter commendAdapter = new CommendAdapter(getContext(),commendList);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(commendAdapter);
    }

    @Override
    protected void initSpringView() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }
}
