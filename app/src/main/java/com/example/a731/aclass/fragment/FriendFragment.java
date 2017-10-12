package com.example.a731.aclass.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.FriendAdapter;
import com.example.a731.aclass.presenter.FriendPresenter;
import com.example.a731.aclass.presenter.impl.FriendPresenterImpl;
import com.example.a731.aclass.view.FriendView;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/10/9.
 */

public class FriendFragment extends BaseFragment implements FriendView{

    private List<String> resource;

    private RecyclerView mRecyclerView;
    private FriendAdapter adapter;
    private FriendPresenter friendPresenter = new FriendPresenterImpl(this);

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_friend;
    }

    @Override
    public void initView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.fragment_friend_recycler_view);
        adapter = new FriendAdapter(resource,getContext());
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
    public void onResume() {
        super.onResume();
        friendPresenter.getFriendList();
    }

    @Override
    public void onGetFriendsSuccess(List<String> username) {
        adapter.onDataChanged(username);
        showToast("获取好友列表成功"+username.size());
    }

    @Override
    public void onGetFriendsFail(String message) {
        showToast("获取好友列表失败:"+message);
    }
}
