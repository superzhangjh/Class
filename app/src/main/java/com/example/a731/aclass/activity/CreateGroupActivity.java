package com.example.a731.aclass.activity;

import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.a731.aclass.R;
import com.example.a731.aclass.view.CreateGroupView;

import java.io.File;

/**
 * Created by 郑选辉 on 2017/9/20.
 */

public class CreateGroupActivity extends BaseActivity implements CreateGroupView{

    private ImageView gHeadImg;
    private EditText gName;
    private Button create;

    private File gHeadImgFile;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    private static final int CAPTURE_PIICTURE_RESULT_CODE = 1001;
    private static final int CAPTURE_PHOTO_RESULT_CODE = 1002;
    private static final int REQUIRE_PERMISSION_REQUEST_CODE = 2001;



    @Override
    protected int getLayoutRes() {
        return R.layout.activity_create_group;
    }

    @Override
    public void initView() {
        gHeadImg = (ImageView) findViewById(R.id.create_group_img_head);
        gName = (EditText) findViewById(R.id.create_group_edt_name);
        create = (Button) findViewById(R.id.create_group_btn_create);
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
                if (name.equals("")){
                    Toast.makeText(CreateGroupActivity.this,"圈名不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (gHeadImgFile == null){
                    Toast.makeText(CreateGroupActivity.this,"圈头像还未选择",Toast.LENGTH_SHORT).show();
                    return;
                }


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
            gHeadImgFile = getRealPathFromFile(uri);
        }else if(requestCode == CAPTURE_PHOTO_RESULT_CODE){
            Uri uri = data.getData();
            gHeadImg.setImageURI(uri);
            gHeadImgFile = new File(uri.toString());
        }

    }

    @Override
    public void initData() {
    }

    public File getRealPathFromFile(Uri uri){
        ContentResolver resolver = this.getContentResolver();
        String[] pojo = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, pojo, null,null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(pojo[0]));
        File file = null;
        if (path!=null && path.length()>0){
            file = new File(path);
        }
        return file;
    }
}
