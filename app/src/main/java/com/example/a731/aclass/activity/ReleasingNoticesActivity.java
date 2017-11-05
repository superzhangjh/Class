package com.example.a731.aclass.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.PhotoAdapter;
import com.example.a731.aclass.data.Notice;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.ReleasingNoticesPresenter;
import com.example.a731.aclass.presenter.impl.ReleasingNoticesPresenterImpl;
import com.example.a731.aclass.util.DateUtil;
import com.example.a731.aclass.util.ImageLoderUtil;
import com.example.a731.aclass.util.SharedPreferencesUtil;
import com.example.a731.aclass.view.ReleasingNoticesView;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobUser;

import static com.example.a731.aclass.util.EaseMobUtil.MODIFIED_RESULT;

/**
 * 发起投票
 * Created by Administrator on 2017/10/17/017.
 */

public class ReleasingNoticesActivity extends BaseActivity implements ReleasingNoticesView{

    private EditText edtTitle;
    private EditText edtContent;
    private TextView tvAddPhoto;
    private TextView tvAddDocument;
    private TextView tvAddSounds;
    private ImageView imgCross;
    private ImageView imgCheck;
    private RecyclerView recPhoto;

    private List<String> mImgs = new ArrayList<>();//图片选择器
    private ReleasingNoticesPresenter presenter = new ReleasingNoticesPresenterImpl(this);
    private String presentGroupId;
    private Notice notice;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_releasing_notices;
    }

    @Override
    public void initView() {
        edtTitle = (EditText) findViewById(R.id.releasing_notices_edtTitle);
        edtContent = (EditText) findViewById(R.id.releasing_notices_edt);
        tvAddPhoto = (TextView) findViewById(R.id.releasing_notices_tv_photo);
        tvAddDocument = (TextView) findViewById(R.id.releasing_notices_tv_document);
        tvAddSounds = (TextView) findViewById(R.id.releasing_notices_tv_sounds);
        imgCross = (ImageView) findViewById(R.id.releasing_notices_cross);
        imgCheck = (ImageView) findViewById(R.id.releasing_notices_check);
        recPhoto = (RecyclerView) findViewById(R.id.releasing_notices_recycler_photo);
    }

    @Override
    public void initListener() {
        //退出
        imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //发布通知
        releasingNotice();

        //添加图片附件
        addImage();

        //添加文档附件
        addDocument();

        //添加录音附件
        addVoice();
    }

    private void addDocument() {
        tvAddDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:打开文件管理器,仅支持ppt,word,excll，和zip
            }
        });
    }

    private void addVoice() {
        tvAddSounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:录音功能
            }
        });
    }

    private void addImage() {
        tvAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new android.support.v7.app.AlertDialog.Builder(ReleasingNoticesActivity.this).setTitle("选择图片")
                        .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String state = Environment.getExternalStorageState();
                                if (state.equals(Environment.MEDIA_MOUNTED)) {
                                    Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(getImageByCamera, ImageLoderUtil.CAPTURE_PHOTO_RESULT_CODE);
                                }
                                else {
                                    showToast("请确认已经插入SD卡");
                                }
                            }
                        }).setNegativeButton("图库", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(ReleasingNoticesActivity.this,ImagePickerActivity.class);
                        startActivityForResult(intent,ImageLoderUtil.CAPTURE_PIICTURE_RESULT_CODE);
                    }
                }).show();
            }
        });
    }

    //发布通知
    private void releasingNotice() {
        imgCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtTitle.getText().toString().equals("")){
                    showToast("标题不能为空");
                    edtTitle.requestFocus();
                    return;
                }else if (edtContent.getText().toString().equals("")){
                    showToast("请输入内容");
                    edtContent.requestFocus();
                    return;
                } else {
                    AlertDialog.Builder  dialog = new AlertDialog.Builder (v.getContext());
                    dialog.setMessage("确认发布该通知？");
                    dialog.setPositiveButton("发布",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showProgress("正在保存数据");
                                    String title = edtTitle.getText().toString().trim();
                                    String content = edtContent.getText().toString();

                                    notice = new Notice();
                                    notice.setPhotoList(mImgs);
                                    notice.setDate(DateUtil.yyyyMMdd_hhmmss());
                                    notice.setTitle(title);
                                    notice.setContent(content);
                                    notice.setCreator(BmobUser.getCurrentUser(Users.class));

                                    //联网上传数据
                                    presenter.saveNotice(presentGroupId,notice,mImgs);
                                }
                            });
                    dialog.setNegativeButton("关闭",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    // 显示
                    dialog.show();
                }
            }
        });
    }

    @Override
    public void initData() {
        presentGroupId = SharedPreferencesUtil.lodaDataFromSharedPreferences(BmobUser.getCurrentUser().getUsername(),this);    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null){
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ImageLoderUtil.CAPTURE_PIICTURE_RESULT_CODE){
            mImgs.addAll(data.getStringArrayListExtra("selectImg"));
            setPhotoList(mImgs);
        }else if(requestCode == ImageLoderUtil.CAPTURE_PHOTO_RESULT_CODE){
            if (resultCode == Activity.RESULT_OK) {
                String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
                FileOutputStream b = null;
                File file = new File("/sdcard/myImage/");
                file.mkdirs();// 创建文件夹
                String fileName = "/sdcard/myImage/"+name;
                try {
                    b = new FileOutputStream(fileName);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
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
                mImgs.add(fileName);
                setPhotoList(mImgs);
            }
        }
    }

    private void setPhotoList(List<String> mImgs) {
        PhotoAdapter adapter = new PhotoAdapter(this,mImgs);
        recPhoto.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recPhoto.setAdapter(adapter);
    }

    @Override
    public void onSaveNoticeSuccess() {
        hideProgress();
        showToast("保存通知成功");
        finish();
    }

    @Override
    public void onSaveNoticeFail(String message) {
        hideProgress();
        showToast("保存通知失败"+message);
    }
}
