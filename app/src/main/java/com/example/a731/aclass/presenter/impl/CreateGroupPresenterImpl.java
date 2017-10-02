package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.CreateGroupPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.view.CreateGroupView;

import java.io.File;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 郑选辉 on 2017/9/27.
 */

public class CreateGroupPresenterImpl implements CreateGroupPresenter{

    private CreateGroupView mCreateGroupView;

    public CreateGroupPresenterImpl (CreateGroupView createGroupView){
        mCreateGroupView = createGroupView;
    }

    @Override
    public void createGroup(String name, Users creator, File headImg) {
        BmobUtil.createGroup(name, creator, headImg, new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    mCreateGroupView.onCreateSuccess();
                }else{
                    mCreateGroupView.onCreateFail(e.getMessage());
                }

            }
        });
    }
}
