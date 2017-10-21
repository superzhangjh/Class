package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.CreateGroupPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.util.ThreadUtils;
import com.example.a731.aclass.view.CreateGroupView;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

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


    @Override
    public void checkGroup(final String name, final Users creator, final String headImg) {

            ThreadUtils.runOnBackgroundThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMGroup group = EaseMobUtil.createGroup(name);
                        if (group!=null){
                            final String groupId = group.getGroupId();
                            BmobUtil.getGroupByField("groupName",name, new FindListener<Group>() {
                                @Override
                                public void done(List<Group> list, final BmobException e) {
                                    if (e == null && (list==null || list.size()==0)){
                                        createGroup(groupId,name,creator,headImg);
                                    }else if (list.size()>0){
                                        mCreateGroupView.onCreateFail("班圈名已存在");
                                    }else{
                                        ThreadUtils.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                mCreateGroupView.onCreateFail("bmob:"+e.getMessage());
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        mCreateGroupView.onCreateFail("easemob:"+e.getMessage()+":"+e.getErrorCode());
                    }
                }
            });



    }

    private void createGroup(final String groupId, final String name, Users creator, final String filePath){
        BmobUtil.createGroup(groupId,name, creator, filePath, new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    uploadImg(filePath,groupId);
                }else{
                    mCreateGroupView.onCreateFail("bmob3:"+e.getMessage()+":"+e.getErrorCode());
                }
            }
        });
    }

    private void uploadImg(String filePath, final String groupId){
        final BmobFile bmobFile = new BmobFile(new File(filePath));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                imgFilPath = bmobFile.getFileUrl();
                findGroup(groupId);
            }
        });
    }

    private void findGroup(String groupId) {
        BmobUtil.getGroupByField("groupId",groupId, new FindListener<Group>() {
            @Override
            public void done(List<Group> list, BmobException e) {
                updateGroup(list.get(0).getObjectId());
            }
        });
    }

    private void updateGroup(String objectId){
        Group group = new Group();
        group.setHeadImg(imgFilPath);
        BmobUtil.updateGroup(objectId, group, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    mCreateGroupView.onCreateSuccess();
                }else{
                    mCreateGroupView.onCreateFail("bmob2:"+e.getMessage()+":"+e.getErrorCode());
                }
            }
        });
    }
}
