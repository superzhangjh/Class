package com.example.a731.aclass.presenter.impl;

import android.graphics.Bitmap;
import android.text.format.DateFormat;

import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.CreateGroupPresenter;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.util.QRCodeUtil;
import com.example.a731.aclass.util.ThreadUtils;
import com.example.a731.aclass.view.CreateGroupView;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by 郑选辉 on 2017/9/27.
 */

public class CreateGroupPresenterImpl implements CreateGroupPresenter {

    private CreateGroupView mCreateGroupView;

    public CreateGroupPresenterImpl (CreateGroupView createGroupView){
        mCreateGroupView = createGroupView;
    }


    @Override
    public void checkGroup(final String name, final Users creator, final String headImg, final Bitmap logo) {

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
                                    Bitmap qrCode = QRCodeUtil.createImage(1,groupId,logo);
                                    String QRCode = saveBitmapToFile(qrCode);
                                    createGroup(groupId,name,creator,headImg,QRCode);
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

    private void createGroup(final String groupId, final String name, Users creator, final String filePath, final String QRCode){
        BmobUtil.createGroup(groupId,name, creator, filePath, new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e==null){
                    uploadImg(filePath,s);
                    upLoadQRCode(QRCode,s);
                }else{
                    mCreateGroupView.onCreateFail("bmob3:"+e.getMessage()+":"+e.getErrorCode());
                }
            }
        });
    }

    private void upLoadQRCode(final String qrCode, final String objectId) {
        final BmobFile bmobFile = new BmobFile(new File(qrCode));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                String QRCode = bmobFile.getFileUrl();
                Group group = new Group();
                group.setQRCode(QRCode);
                updateGroup(group,objectId);
            }
        });
    }

    private void uploadImg(String filePath, final String objectId){
        final BmobFile bmobFile = new BmobFile(new File(filePath));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                String imgFilPath = bmobFile.getFileUrl();
                Group group = new Group();
                group.setHeadImg(imgFilPath);
                updateGroup(group,objectId);
            }
        });
    }


    private void updateGroup(Group group,String objectId){
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
