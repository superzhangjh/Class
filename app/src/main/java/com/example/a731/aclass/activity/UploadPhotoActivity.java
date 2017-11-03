package com.example.a731.aclass.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Gallery;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/5/005.
 */

public class UploadPhotoActivity extends BaseActivity {

    private ImageView imgPhoto;
    private EditText edtIntro;
    private Button btnCannel;
    private Button btnIssue;
    private Gallery gallery;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_uploadphoto;
    }

    @Override
    public void initView() {
        imgPhoto = (ImageView) findViewById(R.id.uploadphoto_edt_image);
        edtIntro = (EditText) findViewById(R.id.uploadphoto_edt_intro);
        btnCannel = (Button) findViewById(R.id.uploadphoto_edt_cannel);
        btnIssue = (Button) findViewById(R.id.uploadphoto_edt_issue);


    }

    @Override
    public void initListener() {
        btnCannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        gallery = new Gallery();


        //获取到的图片路径,请直接使用👇
        List<String> path = new ArrayList<>();
        path.addAll(getIntent().getStringArrayListExtra("imgsPath"));

        Glide.with(this).load(path.get(0)).into(imgPhoto);
    }
}
