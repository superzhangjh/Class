package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.FriendPresenter;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.util.ThreadUtils;
import com.example.a731.aclass.view.FriendView;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/10/9.
 */

public class FriendPresenterImpl implements FriendPresenter {

    private FriendView mFriendView;
    private List<String> friendList;

    public FriendPresenterImpl(FriendView friendView){
        this.mFriendView = friendView;
    }

    @Override
    public void getFriendList() {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    friendList = EaseMobUtil.getFriends();
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mFriendView.onGetFriendsSuccess(friendList);
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mFriendView.onGetFriendsFail(e.getMessage()+"---"+e.getErrorCode());
                        }
                    });

                }
            }
        });
    }
}
