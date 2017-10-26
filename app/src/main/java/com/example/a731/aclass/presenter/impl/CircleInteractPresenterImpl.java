package com.example.a731.aclass.presenter.impl;

import android.util.Log;

import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Vote;
import com.example.a731.aclass.presenter.CircleInteractPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.view.CircleInteractView;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 郑选辉 on 2017/10/26.
 */

public class CircleInteractPresenterImpl implements CircleInteractPresenter {
    private CircleInteractView mView;
    public CircleInteractPresenterImpl(CircleInteractView view){
        mView = view;
    }

    @Override
    public void getGroupVote(String presentGroupId) {
        BmobUtil.getGroupByField("groupId", presentGroupId, new FindListener<Group>() {
            @Override
            public void done(List<Group> list, BmobException e) {
                if (e == null){
                    queryVote(list.get(0).getObjectId());
                }else{
                    mView.onGetVoteFail(e.getMessage());
                }
            }
        });

    }

    private void queryVote(String groupObjectId) {
        BmobUtil.queryVote(groupObjectId, new FindListener<Vote>() {
            @Override
            public void done(List<Vote> list, BmobException e) {
                if (e==null){
                    mView.onGetVoteSuccess(list);
                }else{
                    mView.onGetVoteFail(e.getMessage());
                }
            }
        });
    }
}
