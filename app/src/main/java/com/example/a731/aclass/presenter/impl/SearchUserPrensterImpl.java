package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.SearchUserPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.util.ThreadUtils;
import com.example.a731.aclass.view.SearchUserView;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 郑选辉 on 2017/10/9.
 */

public class SearchUserPrensterImpl implements SearchUserPresenter {

    private SearchUserView mSearchFriendView;

    public SearchUserPrensterImpl(SearchUserView searchFriendView){
        mSearchFriendView = searchFriendView;
    }


    @Override
    public void searchUser(String username) {
        BmobUtil.queryUser(username, new FindListener<Users>() {
            @Override
            public void done(List<Users> list, BmobException e) {
                if (e == null){
                    mSearchFriendView.onSearchUserSuccess(list);
                }else {
                    mSearchFriendView.onSearchuserFail(e.getMessage());
                }
            }
        });
    }

    @Override
    public void addFriend(final String username, final String reason) {

            ThreadUtils.runOnBackgroundThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        EaseMobUtil.addFriendd(username,reason);
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSearchFriendView.onAddFriendSuccess();
                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mSearchFriendView.onAddFriendFail(e.getMessage());
                            }
                        });

                    }
                }
            });

    }
}
