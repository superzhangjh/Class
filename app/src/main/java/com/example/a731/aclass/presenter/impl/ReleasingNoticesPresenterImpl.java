package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Notice;
import com.example.a731.aclass.presenter.ReleasingNoticesPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.view.ReleasingNoticesView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by 郑选辉 on 2017/10/25.
 */

public class ReleasingNoticesPresenterImpl implements ReleasingNoticesPresenter {

    private List<String> imgList = new ArrayList<>();
    private String objectId;

    private ReleasingNoticesView mView;
    public ReleasingNoticesPresenterImpl(ReleasingNoticesView view){
        mView = view;
    }

    @Override
    public void saveNotice(final String presentGroupId, final Notice notice, final List<String> mImgs) {
        if ( mImgs!=null && mImgs.size()>0){
            for (String imgPath:mImgs){
                final BmobFile bmobFile = new BmobFile(new File(imgPath));
                bmobFile.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            imgList.add(bmobFile.getFileUrl());
                            if (imgList.size() == mImgs.size()){
                                addNotice(notice,presentGroupId);
                            }
                        }else{
                            mView.onSaveNoticeFail(e.getMessage());
                        }
                    }
                });
            }
        }else{
            addNotice(notice,presentGroupId);
        }


    }

    private void addNotice(Notice notice, final String presentGroupId){
        BmobUtil.addNotice(notice, new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    objectId = s;
                    updateNotice(presentGroupId);
                }else{
                    mView.onSaveNoticeFail(e.getMessage());
                }
            }
        });
    }


    private void updateNotice(String presentGroupId) {
        BmobUtil.getGroupByField("groupId", presentGroupId, new FindListener<Group>() {
            @Override
            public void done(List<Group> list, BmobException e) {
                if (e == null){
                    Notice notice = new Notice();
                    notice.setPhotoList(imgList);
                    Group group = new Group();
                    group.setObjectId(list.get(0).getObjectId());
                    notice.setGroup(group);
                    BmobUtil.updateNotice(notice, objectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                mView.onSaveNoticeSuccess();
                            }else{
                                mView.onSaveNoticeFail(e.getMessage());
                            }
                        }
                    });
                }else{
                    mView.onSaveNoticeFail(e.getMessage());
                }
            }
        });
    }
}
