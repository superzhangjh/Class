package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.CirclePresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.view.CircleView;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 郑选辉 on 2017/11/8.
 */

public class CirclePresenterImpl implements CirclePresenter {

    private CircleView mView;

    private boolean isAdmin = false;
    public CirclePresenterImpl(CircleView view){
        mView = view;
    }

    public void queryAdmin(String groupId) {
        BmobUtil.getGroupByField("groupId", groupId, new FindListener<Group>() {
            @Override
            public void done(List<Group> list, BmobException e) {
                if (e == null) {
                    BmobUtil.queryGroupAdmin(list.get(0).getObjectId(), new FindListener<Users>() {
                        @Override
                        public void done(List<Users> list, BmobException e) {
                            if (e == null) {
                                if (list.contains(BmobUser.getCurrentUser(Users.class))) {
                                    isAdmin = true;
                                }
                                mView.onGetLimitSuccess(isAdmin);
                            } else {
                                mView.onGetAdminFail(e.getMessage());
                            }
                        }
                    });
                } else {
                    mView.onGetAdminFail(e.getMessage());
                }
            }
        });


    }

}
