package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.CreateGroupPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.view.CreateGroupView;

import java.io.File;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by 郑选辉 on 2017/9/27.
 */

public class CreateGroupPresenterImpl implements CreateGroupPresenter{

    private CreateGroupView mCreateGroupView;
    private String imgFilPath;

    public CreateGroupPresenterImpl (CreateGroupView createGroupView){
        mCreateGroupView = createGroupView;
    }


    //TODO:修改成先创建班圈，后上传头像图片
    @Override
    public void checkGroup(final String name, final Users creator, final String headImg) {
        BmobUtil.getGroupIdByName(name, new FindListener<Group>() {
            @Override
            public void done(List<Group> list, BmobException e) {
                if (e == null && list == null){
                    createGroup(name,creator,headImg);
                }else{
                    mCreateGroupView.onCreateFail(e.getMessage());
                }
            }
        });
    }

    private void createGroup(final String name, Users creator, final String filePath){
        BmobUtil.createGroup(name, creator, filePath, new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    upLoadImg(filePath);
                    BmobUtil.getGroupIdByName(name, new FindListener<Group>() {
                        @Override
                        public void done(List<Group> list, BmobException e) {
                            Group group = new Group();
                            group.setHeadImg(imgFilPath);
                            BmobUtil.updateGroup(list.get(0).getObjectId(), group, new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null){
                                        mCreateGroupView.onCreateSuccess();
                                    }else{
                                        mCreateGroupView.onCreateFail(e.getMessage());
                                    }
                                }
                            });
                        }
                    });
                }else{
                    mCreateGroupView.onCreateFail(e.getMessage());
                }
            }
        });
    }

    private void upLoadImg(String filePath) {
        final BmobFile bmobFile = new BmobFile(new File(filePath));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                imgFilPath = bmobFile.getFileUrl();
            }
        });
    }
}
