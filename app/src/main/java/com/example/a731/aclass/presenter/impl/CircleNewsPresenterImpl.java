package com.example.a731.aclass.presenter.impl;

import android.util.Log;

import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.News;
import com.example.a731.aclass.presenter.CircleNewsPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.view.CircleNewsView;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 郑选辉 on 2017/11/5.
 */

public class CircleNewsPresenterImpl implements CircleNewsPresenter {
    private CircleNewsView mView;
    public CircleNewsPresenterImpl(CircleNewsView view){
        mView = view;
    }

    @Override
    public void getGroupNews(String presentGroupId) {
        BmobUtil.getGroupByField("groupId", presentGroupId, new FindListener<Group>() {
            @Override
            public void done(List<Group> list, BmobException e) {
                if (e == null){
                    queryNews(list.get(0).getObjectId());
                }else{
                    mView.onGetGroupFail(e.getMessage());
                }
            }
        });
    }

    private void queryNews(String objectId) {
        Log.i("CircleNotice",objectId);
        BmobUtil.queryNews(objectId, new FindListener<News>() {
            @Override
            public void done(List<News> list, BmobException e) {
                if (e == null){
                    Log.i("CircleNotice",list.size()+"");
                    mView.onGetNewsSuccess(list);
                }else{
                    mView.onGetNewsFail(e.getMessage());
                }
            }
        });
    }
}
