package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.MainPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.util.ThreadUtils;
import com.example.a731.aclass.view.MainView;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 郑选辉 on 2017/9/27.
 */

public class MainPresenterImpl implements MainPresenter {

    private MainView mMainView;
    private List<Users> friendList;
    private List<String> usernames;
    private List<Group> groupList;
    public MainPresenterImpl(MainView mainView){
        mMainView = mainView;
    }

    public void logOut(){
        BmobUtil.logOut();
        EaseMobUtil.logout(new EMCallBack() {
            @Override
            public void onSuccess() {
                mMainView.onLogoutSuccess();
            }

            @Override
            public void onError(int code, String error) {
                mMainView.onLogoutFail(error);
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    @Override
    public void checkConnectionState() {
        EaseMobUtil.onConnectingListener(new EMConnectionListener() {
            @Override
            public void onConnected() {

            }

            @Override
            public void onDisconnected(int errorCode) {
                mMainView.onDisconnected(errorCode);
            }
        });
    }

    @Override
    public void getFriendList() {
        friendList = new ArrayList<>();
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    usernames = EaseMobUtil.getFriends();
                    for (int i=0;i<usernames.size();i++){
                        Users users = new Users();
                        users.setUsername(usernames.get(i));
                        friendList.add(users);
                    }
                    /*for (int i = 0;i<usernames.size();i++){
                        BmobUtil.queryUser(usernames.get(i), new FindListener<Users>() {
                            @Override
                            public void done(List<Users> list, BmobException e) {
                                if (e == null){
                                    Users users = list.get(0);
                                    friendList.add(users);
                                }else{
                                    mMainView.onGetFriendsFail(e.getMessage());
                                }
                            }
                        });
                    }*/
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMainView.onGetFriendsSuccess(friendList);
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mMainView.onGetFriendsFail(e.getMessage()+"---"+e.getErrorCode());
                        }
                    });

                }
            }
        });
    }

    @Override
    public void getGroup() {
        groupList = new ArrayList<>();
        try {
            List<EMGroup> emGroupList = EaseMobUtil.getAllGroup();
            mMainView.onGetGroupSuccess(emGroupList);
        } catch (HyphenateException e) {
            e.printStackTrace();
            mMainView.onGetGroupFail(e.getMessage());
        }
    }


}
