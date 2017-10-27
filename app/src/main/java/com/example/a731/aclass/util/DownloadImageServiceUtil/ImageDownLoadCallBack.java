package com.example.a731.aclass.util.DownloadImageServiceUtil;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by Administrator on 2017/10/27/027.
 */

public interface ImageDownLoadCallBack {
    //下载文件
    void onDownLoadSuccess(File file);

    //下载图片
    void onDownLoadSuccess(Bitmap bitmap);

    void onDownLoadFailed();
}
