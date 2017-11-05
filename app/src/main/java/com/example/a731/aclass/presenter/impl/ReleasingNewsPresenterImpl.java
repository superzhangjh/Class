package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.News;
import com.example.a731.aclass.data.Notice;
import com.example.a731.aclass.presenter.ReleasingNewsPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.view.ReleasingNewsView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import static com.example.a731.aclass.util.BmobUtil.updateNotice;

/**
 * Created by 郑选辉 on 2017/11/5.
 */

public class ReleasingNewsPresenterImpl implements ReleasingNewsPresenter {

    private List<String> imgList = new ArrayList<>();
    private String objectId;

    private ReleasingNewsView mView;
    public ReleasingNewsPresenterImpl(ReleasingNewsView view){
        mView = view;
    }

    @Override
    public void saveNews(final String presentGroupId, final News news, final List<String> mImgs) {
        if ( mImgs!=null && mImgs.size()>0){
            for (String imgPath:mImgs){
                final BmobFile bmobFile = new BmobFile(new File(imgPath));
                bmobFile.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            imgList.add(bmobFile.getFileUrl());
                            if (imgList.size() == mImgs.size()){
                                addNews(news,presentGroupId);
                            }
                        }else{
                            mView.onSaveNoticeFail(e.getMessage());
                        }
                    }
                });
            }
        }else{
            addNews(news,presentGroupId);
        }
    }


    public void addNews(News news, final String presentGroupd) {
        BmobUtil.addNews(news, new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null){
                    objectId = s;
                    updateNews(presentGroupd);
                }else{
                    mView.onSaveNoticeFail(e.getMessage());
                }
            }
        });
    }

    private void updateNews(String presentGroupId) {
        BmobUtil.getGroupByField("groupId", presentGroupId, new FindListener<Group>() {
            @Override
            public void done(List<Group> list, BmobException e) {
                if (e == null){
                    News news = new News();
                    news.setPhotoList(imgList);
                    Group group = new Group();
                    group.setObjectId(list.get(0).getObjectId());
                    news.setGroup(group);
                    BmobUtil.updateNews(news, objectId, new UpdateListener() {
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
