package com.example.a731.aclass.view;

import com.example.a731.aclass.data.Vote;

/**
 * Created by 郑选辉 on 2017/10/26.
 */

public interface VoteInfoView {
    void onGetVoteSuccess(Vote vote);

    void onGetVoteFail(String message);

    void onUpdateVoteSuccess();

    void onUpdateVoteFail(String message);

}
