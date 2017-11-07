package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.data.Signature;
import com.example.a731.aclass.presenter.SignaturePresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.view.SignatureView;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 郑选辉 on 2017/11/7.
 */

public class SignaturePresenterImpl implements SignaturePresenter {
    private SignatureView mView;
    public SignaturePresenterImpl(SignatureView view){
        mView = view;
    }

    @Override
    public void querySignature(String groupObjectId) {
        BmobUtil.querySignature(groupObjectId, new FindListener<Signature>() {
            @Override
            public void done(List<Signature> list, BmobException e) {
                if (e == null){
                    mView.onGetSignatureSuccess(list);
                }else {
                    mView.onGetSignatureFail(e.getMessage());
                }
            }
        });
    }
}
