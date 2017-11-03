package com.example.a731.aclass.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;
import com.example.a731.aclass.util.DownloadImageServiceUtil.DownLoadImageService;
import com.example.a731.aclass.util.DownloadImageServiceUtil.ImageDownLoadCallBack;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2017/10/26/026.
 */

public class PhotoViewActivity extends BaseActivity {
    private static final int MSG_VISIBLE = 3001;
    private static final int MSG_ERROR = 3002;
    private TextView tvIndex;
    private ViewPager mViewPager;
    private TextView tvSave;
    private List<PhotoView> photoViewList = new ArrayList<>();
    private List<String> photoList = new ArrayList<>();
    private PhotoViewAttacher mAttacher;
    private long delayTime = 1000;
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
        return R.layout.activity_photo_view;
    }

    @Override
    public void initView() {
        tvIndex = (TextView) findViewById(R.id.activity_photo_view_tv_index);
        mViewPager = (ViewPager) findViewById(R.id.activity_photo_view_pager);
        tvSave = (TextView) findViewById(R.id.activity_photo_view_tv_save);

        //获取显示图片的来源
        String singlePicture = getIntent().getStringExtra("singlePicture");
        if (singlePicture!=null && !singlePicture.equals("")){//单张图片显示
            Log.i("eee","单张图片");
            photoList.add(singlePicture);
        }else {//多张图片显示
            String photoJson = getIntent().getStringExtra("photoJson");
            photoList =  new Gson().fromJson(photoJson,ArrayList.class);
            Log.i("eee","多张图片");
        }

        for (int i=0;i<photoList.size();i++){
            PhotoView photoView = new PhotoView(getApplicationContext());
            mAttacher = new PhotoViewAttacher(photoView);
            Glide.with(this).load(photoList.get(i)).error(R.drawable.cross86).into(photoView);
            mAttacher.update();
            photoViewList.add(photoView);
        }

        String getPos = getIntent().getStringExtra("pos");
        final int pos;
        if (getPos!=null){
            pos = Integer.valueOf(getPos);
        }else {
            pos = 0;
        }


        PhotoPagerAdaper adaper = new PhotoPagerAdaper(photoViewList);
        mViewPager.setAdapter(adaper);
        mViewPager.setCurrentItem(pos);
        mViewPager.setOffscreenPageLimit(1);
        tvIndex.setText((pos+1)+"/"+photoList.size());

        final int[] savePosition = {pos};

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                savePosition[0] = position;
                tvIndex.setText((position+1)+"/"+photoList.size());
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //保存文件
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = photoList.get(savePosition[0]);
                onDownLoad(url);
            }
        });
    }

    private void onDownLoad(String url) {

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

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    private static class PhotoPagerAdaper extends PagerAdapter{

        private List<PhotoView> photoViewList;

        public PhotoPagerAdaper(List<PhotoView> photoViewList) {
            this.photoViewList = photoViewList;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position = position % photoViewList.size();
            PhotoView view = photoViewList.get(position);
            ViewParent vp = view.getParent();
            if (vp != null) {
                ViewGroup parent = (ViewGroup) vp;
                parent.removeView(view);
            }
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return photoViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
