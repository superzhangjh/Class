package com.example.a731.aclass.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.CreateGroupPresenter;
import com.example.a731.aclass.presenter.impl.CreateGroupPresenterImpl;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.ImageLoderUtil;
import com.example.a731.aclass.util.SharedPreferencesUtil;
import com.example.a731.aclass.view.CreateGroupView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

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
                showPhotoDialog();
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = gName.getText().toString();
                Users users = BmobUser.getCurrentUser(Users.class);
                Bitmap logo = BitmapFactory.decodeResource(getResources(),R.drawable.default_head_image);
                createGroupPresenter.checkGroup(name, users,gHeadImgPath,logo);
                showProgress("正在创建班圈");
            }
        });
    }

    private void showPhotoDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();//创建一个AlertDialog对象
        dialog.show();//一定要先show出来再设置dialog的参数，不然就不会改变dialog的大小了\
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_select_photo);
        window.setGravity(Gravity.CENTER);//设置对话框在界面底部显示
        dialog.setCanceledOnTouchOutside(true);
        TextView tvTakePhoto = (TextView) window.findViewById(R.id.dialog_select_photo_take);
        TextView tvSelectPhoto = (TextView) window.findViewById(R.id.dialog_select_photo_album);

        //拍照
        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = Environment.getExternalStorageState();
                if (state.equals(Environment.MEDIA_MOUNTED)) {
                    Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(getImageByCamera, ImageLoderUtil.CAPTURE_PHOTO_RESULT_CODE);
                }
                else {
                    showToast("请确认已经插入SD卡");
                }
                dialog.cancel();
            }
        });
        //选择照片
        tvSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,ImageLoderUtil.CAPTURE_PIICTURE_RESULT_CODE);
                dialog.cancel();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null){
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ImageLoderUtil.CAPTURE_PIICTURE_RESULT_CODE){
            Uri uri = data.getData();
            gHeadImg.setImageURI(uri);
            gHeadImgPath = ImageLoderUtil.getRealPathFromUri(this,uri);

        }else if(requestCode == ImageLoderUtil.CAPTURE_PHOTO_RESULT_CODE){



            /*Uri uri = data.getData();
            headImg.setImageURI(uri);
            String gHeadImgPath = uri.toString();
            mainPresenter.updateUserHeadImg(gHeadImgPath);*/


            if (resultCode == Activity.RESULT_OK) {
                String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
                FileOutputStream b = null;
                File file = new File("/sdcard/group_circle/");
                file.mkdirs();// 创建文件夹
                String fileName = "/sdcard/group_circle/"+name;
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
                Glide.with(this).load(fileName).into(gHeadImg);
                gHeadImgPath = fileName;
            }
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
    public void onCreateSuccess(String groupId) {
        hideProgress();
        showToast("创建成功");
        if(SharedPreferencesUtil.lodaDataFromSharedPreferences(BmobUser.getCurrentUser().getUsername(),this) == null){
            SharedPreferencesUtil.saveDataToSharedPreferences(BmobUser.getCurrentUser().getUsername(),groupId,this);
        }
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onCreateFail(String msg) {
        hideProgress();
        showToast("创建失败"+msg);
    }
}
