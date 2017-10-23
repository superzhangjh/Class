package com.example.a731.aclass.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Notice;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.util.DateUtil;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/10/17/017.
 */

public class ReleasingNoticesActivity extends BaseActivity{

    private EditText edtTitle;
    private EditText edtContent;
    private ImageView imgImage;
    private ImageView imgFile;
    private ImageView imgSound;
    private ImageView imgCross;
    private ImageView imgCheck;

    private Notice notice;
    private Users user;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_releasing_notices;
    }

    @Override
    public void initView() {
        edtTitle = (EditText) findViewById(R.id.releasing_notices_edtTitle);
        edtContent = (EditText) findViewById(R.id.releasing_notices_edt);
        imgImage = (ImageView) findViewById(R.id.releasing_notices_imgImage);
        imgFile = (ImageView) findViewById(R.id.releasing_notices_imgFile);
        imgSound = (ImageView) findViewById(R.id.releasing_notices_imgSound);
        imgCross = (ImageView) findViewById(R.id.releasing_notices_cross);
        imgCheck = (ImageView) findViewById(R.id.releasing_notices_check);
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
        imgFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:打开文件管理器,仅支持ppt,word,excll，和zip
            }
        });
    }

    private void addVoice() {
        imgSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:录音功能
            }
        });
    }

    private void addImage() {
        imgImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:创建一个图片拍照工具类，图片选择器工具类
            }
        });
    }

    //发布通知
    private void releasingNotice() {
        imgCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder  dialog = new AlertDialog.Builder (v.getContext());
                dialog.setMessage("确认发布该通知？");
                dialog.setPositiveButton("发布",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String title = edtTitle.getText().toString().trim();
                                String content = edtContent.getText().toString();
                                String date = DateUtil.MMdd_hhss();

                                Intent intent = new Intent();
                                intent.putExtra("title",title);
                                intent.putExtra("content",content);
                                intent.putExtra("date",date);
                                setResult(RESULT_OK,intent);
                                finish();
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
        });
    }

    @Override
    public void initData() {
        notice = new Notice();
        user = BmobUser.getCurrentUser(Users.class);
        int creatorId = user.getId();
    }
}
