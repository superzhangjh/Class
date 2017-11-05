package com.example.a731.aclass.presenter.impl;

import android.util.Log;

import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.QRCodeResultGroupPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.util.ThreadUtils;
import com.example.a731.aclass.view.QRCodeResultGroupView;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 郑选辉 on 2017/11/4.
 */

public class QRCodeResultGroupPresenterImpl implements QRCodeResultGroupPresenter {

    private QRCodeResultGroupView mView;
    public QRCodeResultGroupPresenterImpl(QRCodeResultGroupView view){
        mView = view;
    }


    @Override
    public void joinGroup(final String groupId, final String objectId) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EaseMobUtil.applyJoinToGroup(groupId,null);
                    Group group = new Group();
                    BmobRelation relation = new BmobRelation();
                    Users users = BmobUser.getCurrentUser(Users.class);
                    relation.add(users);
                    group.setMember(relation);
                    BmobUtil.updateGroup(objectId, group, new UpdateListener() {
                        @Override
                        public void done(final BmobException e) {
                            if (e == null){
                                ThreadUtils.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mView.onJoinGroupSuccess();
                                    }
                                });
                            }else{
                                ThreadUtils.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mView.onJoinGroupFail(e.getMessage());
                                    }
                                });
                            }
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mView.onJoinGroupFail(e.getMessage());
                        }
                    });
                }
            }
        });
    }

    @Override
    public void getGroup(String groupId) {
        BmobUtil.getGroupByField("groupId", groupId, new FindListener<Group>() {
            @Override
            public void done(List<Group> list, BmobException e) {
                if (e == null){
                    mView.onGetGroupSuccess(list.get(0));
                }else{
                    mView.onGetGroupFail(e.getMessage());
                }
            }
        });
    }

}
