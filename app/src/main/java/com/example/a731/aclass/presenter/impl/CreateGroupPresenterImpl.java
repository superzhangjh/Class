package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.CreateGroupPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.view.CreateGroupView;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by 郑选辉 on 2017/9/27.
 */

public class CreateGroupPresenterImpl implements CreateGroupPresenter{

    private CreateGroupView mCreateGroupView;

    public CreateGroupPresenterImpl (CreateGroupView createGroupView){
        mCreateGroupView = createGroupView;
    }


    //TODO:修改成先创建班圈，后上传头像图片
    @Override
    public void checkGroup(final String name, final Users creator, String headImg) {
        final BmobFile bmobFile = new BmobFile(new File(headImg));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                String filePath = bmobFile.getFileUrl();
                createGroup(name,creator,filePath);
            }
        });
    }

    private void createGroup(String name,Users creator,String filePath){
        BmobUtil.createGroup(name, creator, filePath, new SaveListener<String>() {
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
