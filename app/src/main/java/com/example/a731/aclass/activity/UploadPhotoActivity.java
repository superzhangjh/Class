package com.example.a731.aclass.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Gallery;

import java.io.File;

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

    }

    @Override
    public void initData() {
        gallery = new Gallery();

        String path = getIntent().getStringExtra("path");
        File file = null;
        if (path!=null){
            file = new File(path);
        }
        if (file.exists()){
            //获取拍摄的图片位置
            Bitmap bitmap= BitmapFactory.decodeFile(path);
            Matrix matrix = new Matrix();
            matrix.postScale(0.8f,0.8f);
            matrix.postRotate(90);
            Bitmap result = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
            imgPhoto.setImageBitmap(result);
        }
    }
}
