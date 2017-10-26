package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.data.Vote;
import com.example.a731.aclass.presenter.VoteInfoPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.view.VoteInfoView;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 郑选辉 on 2017/10/26.
 */

public class VoteInfoPresenterImpl implements VoteInfoPresenter {

    private VoteInfoView mView;

    public VoteInfoPresenterImpl(VoteInfoView view) {
        mView = view;
    }


    @Override
    public void getVoteByObjectId(String objectId) {
        BmobUtil.queryVoteByObjectId(objectId, new QueryListener<Vote>() {
            @Override
            public void done(Vote vote, BmobException e) {
                if (e == null){
                    mView.onGetVoteSuccess(vote);
                }else{
                    mView.onGetVoteFail(e.getMessage());
                }
            }
        });
    }

    @Override
    public void updateVote(String objectId, Vote vote) {
        BmobUtil.updateVote(vote, objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    mView.onUpdateVoteSuccess();
                }else{
                    mView.onUpdateVoteFail(e.getMessage());
                }
            }
        });
    }
}