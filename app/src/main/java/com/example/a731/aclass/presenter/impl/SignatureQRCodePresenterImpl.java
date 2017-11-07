package com.example.a731.aclass.presenter.impl;

import android.util.Log;

import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Signature;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.SignatureQRCodePresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.view.CaptureView;
import com.example.a731.aclass.view.SignatureQRCodeView;

import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 郑选辉 on 2017/11/7.
 */

public class SignatureQRCodePresenterImpl implements SignatureQRCodePresenter {
    private SignatureQRCodeView mSignView;
    private CaptureView mCapView;
    public SignatureQRCodePresenterImpl(SignatureQRCodeView view){
        mSignView = view;
    }

    public SignatureQRCodePresenterImpl(CaptureView view){
        mCapView = view;
    }

    @Override
    public void createSignature(final String classname, String presentGroupId) {
        BmobUtil.getGroupByField("groupId", presentGroupId, new FindListener<Group>() {
            @Override
            public void done(List<Group> list, BmobException e) {
                if (e == null){
                    Signature signature = new Signature();
                    signature.setClassName(classname);
                    signature.setGroup(list.get(0));
                    BmobUtil.createSignature(signature, new SaveListener<String>() {
                        @Override
                        public void done(String objectId, BmobException e) {
                            if (e == null){
                                mSignView.onCreateSignatureSuccess(objectId);
                            }else {
                                Log.i("SignatureQRCodePImpl","createSignature--"+e.getMessage());
                                mSignView.onCreateSignatureFail("createSignature--"+e.getMessage());

                            }
                        }
                    });
                }else {
                    mSignView.onCreateSignatureFail("getGroup--"+e.getMessage());
                    Log.i("SignatureQRCodePImpl","getGroup--"+e.getMessage());
                }
            }
        });
    }

    @Override
    public void updateSignature(String objectId) {
        Signature signature = new Signature();
        BmobRelation relation = new BmobRelation();
        Users users = BmobUser.getCurrentUser(Users.class);
        relation.add(users);
        signature.setHasSign(relation);

        BmobUtil.updateSignature(signature, objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null){
                    mCapView.onSignatureSuccess();
                }else {
                    mCapView.onSignatureFail(e.getMessage());
                }
            }
        });
    }


}
