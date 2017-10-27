package com.example.a731.aclass.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2017/10/26/026.
 */

public class PhotoViewActivity extends BaseActivity {
    private TextView tvIndex;
    private ViewPager mViewPager;
    private TextView tvSave;
    private List<PhotoView> photoViewList = new ArrayList<>();
    private List<String> photoList = new ArrayList<>();
    private PhotoViewAttacher mAttacher;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_photo_view;
    }

    @Override
    public void initView() {
        tvIndex = (TextView) findViewById(R.id.activity_photo_view_tv_index);
        mViewPager = (ViewPager) findViewById(R.id.activity_photo_view_pager);
        tvSave = (TextView) findViewById(R.id.activity_photo_view_tv_save);

        String photoJson = getIntent().getStringExtra("photoJson");
        photoList =  new Gson().fromJson(photoJson,ArrayList.class);

        for (int i=0;i<photoList.size();i++){
            PhotoView photoView = new PhotoView(getApplicationContext());
            mAttacher = new PhotoViewAttacher(photoView);
            Glide.with(this).load(photoList.get(i)).into(photoView);
            mAttacher.update();
            photoViewList.add(photoView);
        }

        int pos = Integer.valueOf(getIntent().getStringExtra("pos"));
        PhotoPagerAdaper adaper = new PhotoPagerAdaper(photoViewList);
        mViewPager.setAdapter(adaper);
        mViewPager.setCurrentItem(pos);
        tvIndex.setText((pos+1)+"/"+photoList.size());
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvIndex.setText((position+1)+"/"+photoList.size());
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
