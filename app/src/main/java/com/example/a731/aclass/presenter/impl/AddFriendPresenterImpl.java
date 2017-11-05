package com.example.a731.aclass.presenter.impl;

import android.util.Log;

import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.AddFriendPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.util.ThreadUtils;
import com.example.a731.aclass.view.AddFriendView;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/11/4/004.
 */

public class AddFriendPresenterImpl implements AddFriendPresenter {

    private AddFriendView mView;
    public AddFriendPresenterImpl(AddFriendView view){
        mView = view;
    }

    @Override
    public void getUser(String username) {
        BmobUtil.queryUser(username, new FindListener<Users>() {
            @Override
            public void done(List<Users> list, BmobException e) {
                if (e==null){
                    mView.onGetUserSuccess(list.get(0));
                }else{
                    mView.onGetUserFail(e.getMessage());
                }
            }
        });
    }

    @Override
    public void addFriend(final String username) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EaseMobUtil.addFriendd(username,null);
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.onAddFriendSuccess();
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.onAddFriendFail(e.getMessage());
                        }
                    });
                }
            }
        });
    }
}
