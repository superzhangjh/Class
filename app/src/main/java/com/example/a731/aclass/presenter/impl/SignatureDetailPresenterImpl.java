package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.SignatureDetailPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.view.SignatureDetailView;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by 郑选辉 on 2017/11/7.
 */

public class SignatureDetailPresenterImpl implements SignatureDetailPresenter {

    private SignatureDetailView mView;
    public SignatureDetailPresenterImpl(SignatureDetailView view){
        mView = view;
    }


    @Override
    public void getSignatureDetail(final String objectId, String groupId) {
        BmobUtil.queryGroupMember(groupId, new FindListener<Users>() {
            @Override
            public void done(final List<Users> groupMembers, BmobException e) {
                if (e == null){
                    BmobUtil.querySignatureMembers(objectId, new FindListener<Users>() {
                        @Override
                        public void done(List<Users> signMembers, BmobException e) {
                            if (e == null){
                                mView.onGetSignatureMemberSuccess(groupMembers,signMembers);
                            }else {
                                mView.onGetSignatureMemberFail(e.getMessage());
                            }
                        }
                    });
                }else {
                    mView.onGetSignatureMemberFail(e.getMessage());
                }
            }
        });
    }
}
