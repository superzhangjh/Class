package com.example.a731.aclass.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.util.DownloadImageServiceUtil.DownLoadImageService;
import com.example.a731.aclass.util.DownloadImageServiceUtil.ImageDownLoadCallBack;
import com.example.a731.aclass.util.QRCodeUtil;
import com.example.a731.aclass.zxing.activity.CaptureActivity;

import java.io.File;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/11/3/003.
 */

public class QRCodeActivity extends BaseActivity {


    private long delayTime = 500;
    private static final int MSG_VISIBLE = 3001;
    private static final int MSG_ERROR = 3002;
    private String headImage;

    private ImageView ImgQRCode;
    private CircleImageView imgHead;
    private TextView tvName;
    private Toolbar toolbar;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what==MSG_VISIBLE){
                showToast("保存成功");
                return true;
            }else if (msg.what==MSG_ERROR){
                showToast("保存失败");
                return true;
            }
            return false;
        }
    });

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_qr_code;
    }

    @Override
    public void initView() {
        ImgQRCode = (ImageView) findViewById(R.id.activity_QRCode_img);
        imgHead = (CircleImageView) findViewById(R.id.activity_QRCode_img_head);
        tvName = (TextView) findViewById(R.id.activity_QRCode_tv_name);
        toolbar = (Toolbar) findViewById(R.id.activity_QRCode_toolbar);

        initToolbar();
        initData();
    }

    private void initToolbar() {
        toolbar.setTitle("通讯录二维码");
        setSupportActionBar(toolbar);

        // 显示标题栏左上角的返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //点击toolbar后导航栏 左上角的图标后，退出当前界面
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initListener() {
        imgHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),PhotoViewActivity.class);
                intent.putExtra("singlePicture",headImage);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        //TODO:启动该Acticity需要从上个页面传3个数据参数
        Intent intent = getIntent();
        String qrCode = intent.getStringExtra("qrCode");
        headImage = intent.getStringExtra("headImage");
        String name = intent.getStringExtra("name");

        if (ImgQRCode!=null && !ImgQRCode.equals("")){
            Glide.with(this).load(qrCode).into(ImgQRCode);
        }
        if (ImgQRCode!=null && !ImgQRCode.equals("")) {
            Glide.with(this).load(headImage).into(imgHead);
        }
        if (ImgQRCode!=null && !ImgQRCode.equals("")) {
            tvName.setText(name);
        }

       /* //测试数据
        String str = "12345678";
        Resources res=getResources();
        Bitmap bmp= BitmapFactory.decodeResource(res, R.drawable.userinfo_head_background);
        Bitmap QRCode = QRCodeUtil.createImage(0,str,300,300,bmp);
        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), QRCode, null,null));
        Glide.with(getApplicationContext()).load(uri).into(ImgQRCode);
        Glide.with(getApplicationContext()).load("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1509740956535&di=7181e23dcd2c47337c31881f12b5ad1c&imgtype=0&src=http%3A%2F%2Fimg.sj33.cn%2Fuploads%2Fallimg%2F201403%2F7-140311233041Q3.png").into(imgHead);
        tvName.setText("麻辣脆皮鸡");*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.qr_code_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_qrCode_scan:
                startActivity(new Intent(getApplicationContext(), CaptureActivity.class));
                break;
            case R.id.menu_qrCode_save:
                String qrCode = getIntent().getStringExtra("qrCode");
                Save(qrCode);
                break;
            case R.id.menu_qrCode_share:
                showToast("分享");
                break;
        }
        return true;
    }

    private void Save(String url) {

        DownLoadImageService service = new DownLoadImageService(getApplicationContext(),
                url, new ImageDownLoadCallBack() {


            @Override
            public void onDownLoadSuccess(File file) {

            }

            @Override
            public void onDownLoadSuccess(Bitmap bitmap) {
                // 在这里执行图片保存方法
                Message message = new Message();
                message.what = MSG_VISIBLE;
                handler.sendMessageDelayed(message,delayTime);

            }

            @Override
            public void onDownLoadFailed() {
                // 图片保存失败
                Message message = new Message();
                message.what = MSG_ERROR;
                handler.sendMessageDelayed(message,delayTime);
            }
        });
        //启动图片下载线程
        new Thread(service).start();
    }
}
