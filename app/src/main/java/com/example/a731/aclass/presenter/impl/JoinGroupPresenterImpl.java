package com.example.a731.aclass.presenter.impl;

import android.util.Log;

import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.JoinGroupPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.util.ThreadUtils;
import com.example.a731.aclass.view.JoinGroupView;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 郑选辉 on 2017/10/19.
 */

public class JoinGroupPresenterImpl implements JoinGroupPresenter {
    private JoinGroupView mJoinGroupView;
    public JoinGroupPresenterImpl(JoinGroupView joinGroupView){
        mJoinGroupView = joinGroupView;
    }

    private final String TAG = "JoinGroupPresenterImpl";

    private Group mGroup;

    @Override
    public void searchGroup(String groupId) {
        BmobUtil.getGroupByField("groupId", groupId, new FindListener<Group>() {
            @Override
            public void done(List<Group> list, BmobException e) {
                if (e==null){
                    mGroup = list.get(0);
                    mJoinGroupView.onSearchGroupSuccess(mGroup);
                }else{
                    mJoinGroupView.onSearchGroupFail(e.getMessage());
                }
            }
        });
    }

    @Override
    public void joinGroup(){
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EaseMobUtil.applyJoinToGroup(mGroup.getGroupId(),null);
                    Group group = new Group();
                    BmobRelation relation = new BmobRelation();
                    Users users = BmobUser.getCurrentUser(Users.class);
                    relation.add(users);
                    group.setMember(relation);
                    BmobUtil.updateGroup(mGroup.getObjectId(), group, new UpdateListener() {
                        @Override
                        public void done(final BmobException e) {
                            if (e == null){
                                ThreadUtils.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i(TAG,"加入班圈成功");
                                        mJoinGroupView.onJoinGroupSuccess();
                                    }
                                });
                            }else{
                                ThreadUtils.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.i(TAG,"加入班圈成功");
                                        mJoinGroupView.onJoinGroupFail(e.getMessage());
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
                            mJoinGroupView.onJoinGroupFail(e.getMessage());
                        }
                    });
                }
            }
        });

    }
}
