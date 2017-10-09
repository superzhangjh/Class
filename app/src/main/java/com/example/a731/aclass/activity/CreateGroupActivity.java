package com.example.a731.aclass.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.CreateGroupPresenter;
import com.example.a731.aclass.presenter.impl.CreateGroupPresenterImpl;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.ImageLoderUtil;
import com.example.a731.aclass.view.CreateGroupView;

import java.io.File;

import cn.bmob.v3.BmobUser;

/**
 * Created by 郑选辉 on 2017/9/20.
 */

public class CreateGroupActivity extends BaseActivity implements CreateGroupView{

    private ImageView gHeadImg;
    private EditText gName;
    private Button create;

    private String gHeadImgPath;

    private CreateGroupPresenter createGroupPresenter;

    private static final int CAPTURE_PIICTURE_RESULT_CODE = 1001;
    private static final int CAPTURE_PHOTO_RESULT_CODE = 1002;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_create_group;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void initView() {
        gHeadImg = (ImageView) findViewById(R.id.create_group_img_head);
        gName = (EditText) findViewById(R.id.create_group_edt_name);
        create = (Button) findViewById(R.id.create_group_btn_create);

        createGroupPresenter = new CreateGroupPresenterImpl(this);
        getPermission();
    }

    @Override
    public void initListener() {
        gHeadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(CreateGroupActivity.this).setTitle("选择图片")
                        .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String state = Environment.getExternalStorageState();
                                if (state.equals(Environment.MEDIA_MOUNTED)) {
                                    Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
                                    startActivityForResult(getImageByCamera, CAPTURE_PHOTO_RESULT_CODE);
                                }
                                else {
                                    Toast.makeText(CreateGroupActivity.this, "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
                                }
                            }
                        }).setNegativeButton("图库", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent,CAPTURE_PIICTURE_RESULT_CODE);
                    }
                }).show();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = gName.getText().toString();
                Users users = BmobUser.getCurrentUser(Users.class);

                createGroupPresenter.checkGroup(name, users,gHeadImgPath);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null){
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURE_PIICTURE_RESULT_CODE){
            Uri uri = data.getData();
            gHeadImg.setImageURI(uri);
            gHeadImgPath = ImageLoderUtil.getRealPathFromUri(this,uri);
        }else if(requestCode == CAPTURE_PHOTO_RESULT_CODE){
            Uri uri = data.getData();
            gHeadImg.setImageURI(uri);
            gHeadImgPath = uri.toString();
        }

    }

    @Override
    public void initData() {
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void getPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
        }
    }

    @Override
    public void onCreateSuccess() {
        showToast("创建成功");
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCreateFail(String msg) {
        showToast("创建失败"+msg);
    }
}
