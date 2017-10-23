package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.presenter.SystematicNotificationPresenter;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.util.ThreadUtils;
import com.example.a731.aclass.view.SystematicNotificationView;
import com.hyphenate.exceptions.HyphenateException;

/**
 * Created by 郑选辉 on 2017/10/21.
 */

public class SystematicNotificationPresenterImpl implements SystematicNotificationPresenter {

    private SystematicNotificationView mNotificationView;

    public SystematicNotificationPresenterImpl(SystematicNotificationView notificationView){
        mNotificationView = notificationView;
    }

    @Override
    public void acceptApplyFriend(final String username) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EaseMobUtil.acceptFriendRequest(username);
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mNotificationView.refresh();
                        }
                    });

                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mNotificationView.onAcceptFriendFail(e.getMessage());
                        }
                    });
                }
            }
        });
    }

    @Override
    public void acceptApplyJoinGroup(final String username, final String groupId) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EaseMobUtil.acceptApplyJoinGroup(username, groupId);
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mNotificationView.refresh();
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mNotificationView.onAcceptApplyJoinGroupFail(e.getMessage());
                        }
                    });
                }
            }
        });
    }

    @Override
    public void refuseApplyFriend(final String username) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EaseMobUtil.refuseFriendRequest(username);
                    mNotificationView.refresh();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    mNotificationView.onRefuseApplyFriendFail(e.getMessage());
                }
            }
        });
    }

    @Override
    public void refuseApplyJoinGroup(final String username, final String groupId) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EaseMobUtil.refuseApplyJoinGroup(username, groupId);
                    mNotificationView.refresh();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    mNotificationView.onRefuseApplyJoinGroupFail(e.getMessage());
                }
            }
        });

    }
}
