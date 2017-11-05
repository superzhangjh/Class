package com.example.a731.aclass.presenter.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.SignUpPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.util.QRCodeUtil;
import com.example.a731.aclass.util.ThreadUtils;
import com.example.a731.aclass.view.SignUpView;
import com.hyphenate.EMCallBack;
import com.hyphenate.exceptions.HyphenateException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by 郑选辉 on 2017/9/27.
 */

public class SignUpPresenterImpl implements SignUpPresenter{

    private SignUpView mSignUpView;
    private static final String TAG = "SignupPresenterImpl";

    public SignUpPresenterImpl(SignUpView signUpView){
        mSignUpView = signUpView;
    }

    @Override
    public void requestSMSCode(String phoneNum) {
        BmobUtil.requestSMSCode(phoneNum, new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    mSignUpView.onRequestSMSCodeSuccess();
                }else{
                    mSignUpView.onRequestSMSCodeFail("bmob:"+e.getMessage());
                    Log.i(TAG,e.getMessage()+"--"+e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void register(final String userName, final String phoneNum, final String password, final String smsCode, final Bitmap logo) {
        ThreadUtils.runOnBackgroundThread(new Runnable() {
            @Override
            public void run() {
                try {
                    EaseMobUtil.signUp(userName,password);
                    Bitmap qrCode = QRCodeUtil.createImage(0,userName,logo);
                    String filePath = saveBitmapToFile(qrCode);
                    registerBmob(userName,phoneNum,password,smsCode,filePath);
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSignUpView.onRegisterFail("Mob:"+e.getMessage()+e.getErrorCode());
                        }
                    });
                }
            }
        });
    }

    private void registerBmob(final String userName, String phoneNum, final String password, String smsCode, final String filePath) {
        BmobUtil.register(userName, phoneNum, password, smsCode, new SaveListener<Users>() {
            @Override
            public void done(Users users, final BmobException e) {
                if (e == null){
                    final BmobFile bmobFile = new BmobFile(new File(filePath));
                    bmobFile.uploadblock(new UploadFileListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                final String imgPath = bmobFile.getFileUrl();
                                Users users = new Users();
                                users.setQRCode(imgPath);
                                BmobUtil.updateUser(users, BmobUser.getCurrentUser(Users.class).getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e == null){
                                            ThreadUtils.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    mSignUpView.onRegisterSuccess();
                                                    EaseMobUtil.login(userName, password, new EMCallBack() {
                                                        @Override
                                                        public void onSuccess() {
                                                            mSignUpView.onRegisterSuccess();
                                                        }

                                                        @Override
                                                        public void onError(int code, String error) {

                                                        }

                                                        @Override
                                                        public void onProgress(int progress, String status) {

                                                        }
                                                    });
                                                }
                                            });
                                        }else{
                                            mSignUpView.onRegisterFail(e.getMessage());
                                        }
                                    }
                                });
                            }else{
                                mSignUpView.onRegisterFail(e.getMessage());
                            }

                        }
                    });

                }else{
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSignUpView.onRegisterFail("Bmob:"+e.getMessage()+e.getErrorCode());
                        }
                    });
                }
            }
        });
    }

    private String saveBitmapToFile(Bitmap qrCode){
        String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";

        FileOutputStream b = null;
        File file = new File("/sdcard/myImage/");
        file.mkdirs();// 创建文件夹
        String fileName = "/sdcard/myImage/"+name;
        try {
            b = new FileOutputStream(fileName);
            qrCode.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                try {
                    b.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return fileName;
    }
}
