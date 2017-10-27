package com.example.a731.aclass.activity;

import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2017/10/26/026.
 */

public class PhotoViewActivity extends BaseActivity{
    private TextView tvIndex;
    private PhotoView imgPhotoView;
    private TextView tvSave;
    private PhotoViewAttacher mAttacher;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_photo_view;
    }

    @Override
    public void initView() {
        tvIndex = (TextView) findViewById(R.id.activity_photo_view_tv_index);
        imgPhotoView = (PhotoView) findViewById(R.id.activity_photo_view_img);
        tvSave = (TextView) findViewById(R.id.activity_photo_view_tv_save);

        String uri = getIntent().getStringExtra("photoUri");
        mAttacher = new PhotoViewAttacher(imgPhotoView);
        Glide.with(this).load(uri).into(imgPhotoView);
        mAttacher.update();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }
}
