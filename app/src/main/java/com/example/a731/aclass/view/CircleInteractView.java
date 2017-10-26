package com.example.a731.aclass.view;

import com.example.a731.aclass.data.Vote;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/10/26.
 */

public interface CircleInteractView {
    void onGetVoteSuccess(List<Vote> list);

    void onGetVoteFail(String message);
}
