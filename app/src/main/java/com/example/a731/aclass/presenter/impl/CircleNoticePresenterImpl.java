package com.example.a731.aclass.presenter.impl;

import android.util.Log;

import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Notice;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.CircleNoticePresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.view.CircleNoticeView;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 郑选辉 on 2017/10/25.
 */

public class CircleNoticePresenterImpl implements CircleNoticePresenter {
    private CircleNoticeView mView;

    private String groupObjectId;
    private boolean isAdmin = false;
    public CircleNoticePresenterImpl(CircleNoticeView view){
        mView = view;
    }

    @Override
    public void getGroupNotice(String groupId) {
        BmobUtil.getGroupByField("groupId", groupId, new FindListener<Group>() {
            @Override
            public void done(List<Group> list, BmobException e) {
                if (e == null){
                    groupObjectId = list.get(0).getObjectId();
                    queryNotice(list.get(0).getObjectId());
                }else{
                    mView.onGetGroupFail(e.getMessage());
                }
            }
        });
    }

    public boolean queryAdmin() {
        BmobUtil.queryGroupAdmin(groupObjectId, new FindListener<Users>() {
            @Override
            public void done(List<Users> list, BmobException e) {
                if (e == null){
                    if (list.contains(BmobUser.getCurrentUser(Users.class))){
                        isAdmin = true;
                    }
                }else{
                    mView.onGetAdminFail(e.getMessage());
                }
            }
        });
        return isAdmin;
    }

    private void queryNotice(String objectId) {
        Log.i("CircleNotice",objectId);
        BmobUtil.queryNotice(objectId, new FindListener<Notice>() {
            @Override
            public void done(List<Notice> list, BmobException e) {
                if (e == null){
                    Log.i("CircleNotice",list.size()+"");
                    mView.onGetNoticeSuccess(list);
                }else{
                    mView.onGetNoticeFail(e.getMessage());
                }
            }
        });
    }
}
