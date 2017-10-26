package com.example.a731.aclass.presenter;

import com.example.a731.aclass.data.Vote;

/**
 * Created by 郑选辉 on 2017/10/26.
 */

public interface VoteInfoPresenter {

    void getVoteByObjectId(String objectId);

    void updateVote(String objectId, Vote vote);
}
