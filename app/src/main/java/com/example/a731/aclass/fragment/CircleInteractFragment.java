package com.example.a731.aclass.fragment;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/10/6/006.
 */

public class CircleInteractFragment extends BaseFragment implements CircleInteractView{
    private static final int START_A_VOTE = 2001;
    private static final int VOTE_INFO = 2002;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Vote> voteList = new ArrayList<>();
    private InteractAdapter adapter;
    private TextView tvStartVote;
    private String presentGroupId;

    private CircleInteractPresenter presenter = new CircleInteractPresenterImpl(this);

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_circle_interact;
    }

    @Override
    public void initView() {
        tvStartVote = (TextView) mRootView.findViewById(R.id.circle_interact_start_a_vote);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.circle_interact_recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.circle_interact_swiperefresh);

        //发动投票功能
        tvStartVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),StartVoteActivity.class);
                startActivityForResult(intent,START_A_VOTE);
            }
        });

        adapter = new InteractAdapter(getContext(),voteList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
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
    public void initListener() {

    }

    @Override
    public void initData() {
        presentGroupId = SharedPreferencesUtil.lodaDataFromSharedPreferences("groupId",getContext());
        if (presentGroupId != null)
            presenter.getGroupVote(presentGroupId);


    }


    @Override
    public void onGetVoteSuccess(List<Vote> list) {
        showToast("获取投票列表成功"+list.size()+":"+presentGroupId);
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
        if (presentGroupId != null)
        presenter.getGroupVote(presentGroupId);
    }
}
