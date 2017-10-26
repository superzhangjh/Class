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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/10/6/006.
 */

public class CircleInteractFragment extends BaseFragment{
    private static final int START_A_VOTE = 2001;
    private static final int VOTE_INFO = 2002;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Vote> voteList = new ArrayList<>();
    private InteractAdapter adapter;
    private TextView tvStartVote;
    private int index;

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
                String voteJson = new Gson().toJson(vote);
                index = position;
                intent.putExtra("vote",voteJson);
                startActivityForResult(intent,VOTE_INFO);
            }
        });
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
            case START_A_VOTE:
                if (resultCode==RESULT_OK){
                    String voteJson = data.getStringExtra("voteJson");
                    Vote vote = new Gson().fromJson(voteJson,Vote.class);
                    voteList.add(vote);
                    adapter.setListDataChange(voteList);
                }
                break;
            case VOTE_INFO:
                if (resultCode==RESULT_OK){
                    String voteJson = data.getStringExtra("vote");
                    Vote vote = new Gson().fromJson(voteJson,Vote.class);
                    voteList.set(index,vote);
                    adapter.setListDataChange(voteList);
                }
                break;
        }
    }
}
