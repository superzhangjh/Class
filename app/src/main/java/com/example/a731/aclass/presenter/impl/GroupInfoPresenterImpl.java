package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.GroupInfoPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.ThreadUtils;
import com.example.a731.aclass.view.GroupInfoView;

import java.util.List;

import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 郑选辉 on 2017/10/18.
 */

public class GroupInfoPresenterImpl implements GroupInfoPresenter {

    private GroupInfoView mGroupInfoView;

    public GroupInfoPresenterImpl(GroupInfoView groupInfoView){
        this.mGroupInfoView = groupInfoView;
    }


    @Override
    public void getGroupMember(final String groupObjectId) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                BmobUtil.queryGroupMember(groupObjectId, new FindListener<Users>() {
                    @Override
                    public void done(final List<Users> list, final BmobException e) {
                        if (e == null) {
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mGroupInfoView.onGetGroupMemberSuccess(list);
                                }
                            });
                        } else {
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mGroupInfoView.onGetGroupMemberFail(e.getMessage());
                                }
                            });
                        }
                    }
                });
            }
        });


    }

    @Override
    public void getGroup(final String groupId) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                BmobUtil.getGroupByField("groupId", groupId, new FindListener<Group>() {
                    @Override
                    public void done(final List<Group> list, final BmobException e) {
                        if (e == null) {
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mGroupInfoView.onGetGroupSuccess(list);
                                }
                            });

                        }else{
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mGroupInfoView.onGetGroupFail("获取班圈失败:" + e.getMessage() + ":" + e.getErrorCode());
                                }
                            });
                        }
                    }
                });
            }
        });

    }

    @Override
    public void getGroupAdmin(String objectId) {
        BmobUtil.queryGroupAdmin(objectId, new FindListener<Users>() {
            @Override
            public void done(final List<Users> list, final BmobException e) {
                if (e == null) {
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mGroupInfoView.onGetGroupAdminSuccess(list);
                        }
                    });
                } else {
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mGroupInfoView.onGetGroupAdminFail(e.getMessage());
                        }
                    });
                }
            }
        });
    }

    @Override
    public void removeAdmin(Users users, String groupObjectId) {
        Group group = new Group();
        BmobRelation relation = new BmobRelation();
        relation.remove(users);
        group.setAdministrator(relation);
        BmobUtil.updateGroup(groupObjectId, group, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    mGroupInfoView.onAddOrRemoveAdminSuccess();
                }else{
                    mGroupInfoView.onAddOrRemoveAdminFail(e.getMessage());
                }
            }
        });
    }
}
