package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.ModifiedUserDataPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.view.ModifiedUserDataView;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 郑选辉 on 2017/10/25.
 */

public class ModifiedUserDataPresenterImpl implements ModifiedUserDataPresenter {
    private ModifiedUserDataView mView;
    public ModifiedUserDataPresenterImpl(ModifiedUserDataView view){
        mView = view;
    }

    @Override
    public void save(Users user, String objectId) {
        BmobUtil.updateUser(user, objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    mView.onSaveSuccess();
                }else{
                    mView.onSaveFail(e.getMessage());
                }
            }
        });
    }
}
