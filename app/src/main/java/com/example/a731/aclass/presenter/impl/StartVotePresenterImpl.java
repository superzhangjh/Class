package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Notice;
import com.example.a731.aclass.data.Vote;
import com.example.a731.aclass.presenter.StartVotePresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.view.StartVoteView;

import java.io.File;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by 郑选辉 on 2017/10/26.
 */

public class StartVotePresenterImpl implements StartVotePresenter {
    private StartVoteView mView;
    private List<String> imgList;
    private String objectId;
    public StartVotePresenterImpl(StartVoteView view){
        mView = view;
    }

    @Override
    public void saveVote(final String presentGroupId, final Vote vote, final List<String> mImgs) {
        if ( mImgs!=null && mImgs.size()>0){
            for (String imgPath:mImgs){
                final BmobFile bmobFile = new BmobFile(new File(imgPath));
                bmobFile.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            imgList.add(bmobFile.getFileUrl());
                            if (imgList.size() == mImgs.size()){
                                addVote(vote,presentGroupId);
                            }
                        }else{
                            mView.onSaveVoteFail(e.getMessage());
                        }
                    }
                });
            }
        }else{
            addVote(vote,presentGroupId);
        }
    }


    private void addVote(Vote vote, final String presentGroupId){
        BmobUtil.addVote(vote, new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    objectId = s;
                    updateNotice(presentGroupId);
                }else{
                    mView.onSaveVoteFail(e.getMessage());
                }
            }
        });
    }


    private void updateNotice(String presentGroupId) {
        BmobUtil.getGroupByField("groupId", presentGroupId, new FindListener<Group>() {
            @Override
            public void done(List<Group> list, BmobException e) {
                if (e == null){
                    Vote vote = new Vote();
                    vote.setPhotoList(imgList);
                    Group group = new Group();
                    group.setObjectId(list.get(0).getObjectId());
                    vote.setGroup(group);
                    BmobUtil.updateVote(vote, objectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                mView.onSaveVoteSuccess();
                            }else{
                                mView.onSaveVoteFail(e.getMessage());
                            }
                        }
                    });
                }else{
                    mView.onSaveVoteFail(e.getMessage());
                }
            }
        });
    }
}
