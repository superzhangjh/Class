package com.example.a731.aclass.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.activity.StartVoteActivity;
import com.example.a731.aclass.activity.VoteInfoActivity;
import com.example.a731.aclass.adapter.InteractAdapter;
import com.example.a731.aclass.data.Vote;
import com.example.a731.aclass.presenter.CircleInteractPresenter;
import com.example.a731.aclass.presenter.impl.CircleInteractPresenterImpl;
import com.example.a731.aclass.util.SharedPreferencesUtil;
import com.example.a731.aclass.view.CircleInteractView;
import com.google.gson.Gson;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;

import cn.bmob.v3.BmobUser;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/10/6/006.
 */

public class CircleInteractFragment extends BaseFragment implements CircleInteractView{
    private RecyclerView mRecyclerView;
    private List<Vote> voteList = new ArrayList<>();
    private InteractAdapter adapter;
    private String presentGroupId;
    private SpringView springView;

    private CircleInteractPresenter presenter = new CircleInteractPresenterImpl(this);

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_circle_base;
    }

    @Override
    public void initView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.circle_base_recyclerview);
        adapter = new InteractAdapter(getContext(),voteList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemViewCacheSize(20);
        adapter.setOnItemClickListener(new InteractAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getContext(),VoteInfoActivity.class);
                Vote vote = voteList.get(position);
                intent.putExtra("objectId",vote.getObjectId());
                startActivity(intent);
            }
        });
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
                        if (presentGroupId != null)
                            presenter.getGroupVote(presentGroupId);
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

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        presentGroupId = SharedPreferencesUtil.lodaDataFromSharedPreferences(BmobUser.getCurrentUser().getUsername(),getContext());
        if (presentGroupId != null)
            presenter.getGroupVote(presentGroupId);

    }


    @Override
    public void onGetVoteSuccess(List<Vote> list) {
        //showToast("获取投票列表成功"+list.size()+":"+presentGroupId);
        voteList = list;
        adapter.setListDataChange(voteList);
    }

    @Override
    public void onGetVoteFail(String message) {
        showToast("获取投票列表失败："+message);
    }

    @Override
    public void onResume() {
        super.onResume();
        presentGroupId = SharedPreferencesUtil.lodaDataFromSharedPreferences(BmobUser.getCurrentUser().getUsername(),getContext());
        if (presentGroupId != null)
            presenter.getGroupVote(presentGroupId);
    }
}
