package com.example.a731.aclass.presenter;

import com.example.a731.aclass.data.Vote;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/10/26.
 */

public interface StartVotePresenter {
    void saveVote(String presentGroupId, Vote vote, List<String> photoList);
}
