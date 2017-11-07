package com.example.a731.aclass.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.azhon.suspensionfab.FabAttributes;
import com.azhon.suspensionfab.OnFabClickListener;
import com.azhon.suspensionfab.SuspensionFab;
import com.example.a731.aclass.R;
import com.example.a731.aclass.activity.ImagePickerActivity;
import com.example.a731.aclass.activity.ReleasingDocGatheringActivity;
import com.example.a731.aclass.activity.ReleasingNewsActivity;
import com.example.a731.aclass.activity.ReleasingNoticesActivity;
import com.example.a731.aclass.activity.ScheduleActivity;
import com.example.a731.aclass.activity.StartVoteActivity;
import com.example.a731.aclass.activity.UploadPhotoActivity;
import com.example.a731.aclass.adapter.CircleFragmentPagerAdapter;
import com.example.a731.aclass.util.Animation.FabButtonAnimate;
import com.example.a731.aclass.util.ImageLoderUtil;
import com.example.a731.aclass.util.PopItemUtil;
import com.example.a731.aclass.util.ToastUtil;
import com.example.a731.aclass.util.customView.NoScrollViewPager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/9/15/015.
 */

public class CircleFragment extends BaseFragment{

    private TabLayout tablayout;
    private NoScrollViewPager viewpager;
    private CircleFragmentPagerAdapter adapter;
    private List<Fragment> list_fragment;
    private List<String> list_title;
    private SuspensionFab susFab;//浮动按钮
    private TextView tvTakePhoto;
    private TextView tvSelectPhoto;
    private CardView ibFn;
    private TextView tvFnText;

    private int ibFnType = 0;
    private static final int REQUEST_CAMERA = 1001;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_circle;
    }


    @Override
    public void initView() {
        tvTakePhoto = (TextView) mRootView.findViewById(R.id.dialog_select_photo_take);
        tvSelectPhoto = (TextView) mRootView.findViewById(R.id.dialog_select_photo_album);
        ibFn = (CardView) mRootView.findViewById(R.id.circle_fn);
        tvFnText = (TextView) mRootView.findViewById(R.id.circle_fn_text);

        list_fragment = new ArrayList<>();
        //通知
        list_fragment.add(new CircleNoticeFragment());
        //互动
        list_fragment.add(new CircleInteractFragment());
        //动态
        list_fragment.add(new CircleNewsFragment());

        list_title = new ArrayList<>();
        list_title.add("通知");
        list_title.add("互动");
        list_title.add("动态");
        initTabViewpager();
        initSuspensionFab();
    }

    @Override
    protected void initSpringView() {

    }

    //悬浮按钮
    private void initSuspensionFab() {
        susFab = (SuspensionFab) mRootView.findViewById(R.id.circle_suspensionfab);
        //构建展开按钮属性
        //课程表
        FabAttributes schedule = new FabAttributes.Builder()
                .setBackgroundTint(Color.parseColor("#2096F3"))
                .setSrc(getResources().getDrawable(R.drawable.chat_fn))
                .setFabSize(FloatingActionButton.SIZE_AUTO)
                .setPressedTranslationZ(10)
                .setTag(1)
                .build();
        //照片
        FabAttributes album = new FabAttributes.Builder()
                .setBackgroundTint(Color.parseColor("#FF9800"))
                .setSrc(getResources().getDrawable(R.drawable.chat_fn_send))
                .setFabSize(FloatingActionButton.SIZE_AUTO)
                .setPressedTranslationZ(10)
                .setTag(2)
                .build();
        //发布通知
        FabAttributes notice = new FabAttributes.Builder()
                .setBackgroundTint(Color.parseColor("#03A9F4"))
                .setSrc(getResources().getDrawable(R.drawable.chat_sound))
                .setFabSize(FloatingActionButton.SIZE_AUTO)
                .setPressedTranslationZ(10)
                .setTag(3)
                .build();

        //添加菜单
        susFab.addFab(schedule,album,notice);
        susFab.setAnimationManager(new FabButtonAnimate(susFab));
        //菜单点击事件
        susFab.setFabClickListener(new OnFabClickListener() {
            @Override
            public void onFabClick(FloatingActionButton fab, Object tag) {
                String msg = "";
                switch ((int)tag){
                    case 1://课程表
                        Intent intent = new Intent(getContext(), ScheduleActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        msg="上传照片";
                        showPhotoDialog();break;
                    case 3:
                        msg="更多信息";
                        break;
                    default:break;
                }
                showToast(msg);
            }
        });
    }

    private void showPhotoDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(getContext()).create();//创建一个AlertDialog对象
        dialog.show();//一定要先show出来再设置dialog的参数，不然就不会改变dialog的大小了\
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_select_photo);
        window.setGravity(Gravity.CENTER);//设置对话框在界面底部显示
        dialog.setCanceledOnTouchOutside(true);
        tvTakePhoto = (TextView) window.findViewById(R.id.dialog_select_photo_take);
        tvSelectPhoto = (TextView) window.findViewById(R.id.dialog_select_photo_album);

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
                Intent intent = new Intent(getContext(),ImagePickerActivity.class);
                startActivityForResult(intent,ImageLoderUtil.CAPTURE_PIICTURE_RESULT_CODE);
                dialog.cancel();
            }
        });
    }

    private void initTabViewpager() {
        tablayout = (TabLayout) mRootView.findViewById(R.id.circle_tablayout);
        viewpager = (NoScrollViewPager) mRootView.findViewById(R.id.circle_viewpager);
        //设置tablayout显示模式
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        adapter = new CircleFragmentPagerAdapter(getContext(),getActivity().getSupportFragmentManager(),list_fragment,list_title);
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
        tablayout.setSmoothScrollingEnabled(false);

        //设置第一个tablayout样式
        View firstView = adapter.getTabView(0);
        TextView textView = (TextView) firstView.findViewById(R.id.tv_tab_title);
        textView.setTextColor(0xFFffffff);
        tablayout.getTabAt(0).setCustomView(firstView);

        for (int i = 1; i < tablayout.getTabCount(); i++) {
            View tabView = adapter.getTabView(i);
            CardView mageView = (CardView) tabView.findViewById(R.id.iv_tab_red);
            TextView tvRedCount = (TextView) tabView.findViewById(R.id.iv_tab_red_count);
            tablayout.getTabAt(i).setCustomView(tabView);
        }

    }

    @Override
    public void initListener() {

        //tablayout选择监听
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                View customView = tab.getCustomView();
                customView.findViewById(R.id.iv_tab_red).setVisibility(View.GONE);
                TextView textView = (TextView) customView.findViewById(R.id.tv_tab_title);
                textView.setTextColor(0xFFffffff);
                switch (position){
                    case 0:
                        ibFnType=0;
                        tvFnText.setText("通知");
                        break;
                    case 1:
                        ibFnType=1;
                        tvFnText.setText("互动");
                        break;
                    case 2:
                        ibFnType=2;
                        tvFnText.setText("说说");
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View customView = tab.getCustomView();
                TextView textView = (TextView) customView.findViewById(R.id.tv_tab_title);
                textView.setTextColor(0xFFCCCCCC);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ibFnType = position;
                switch (position){
                    case 0:
                        tvFnText.setText("通知");
                        break;
                    case 1:
                        tvFnText.setText("互动");
                        break;
                    case 2:
                        tvFnText.setText("说说");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        //功能按钮作用
        ibFn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (ibFnType){
                    case 0://通知
                        String[] interactTypes = {"发布通知","资料收取"};
                        PopItemUtil popItemUtil = new PopItemUtil(getContext(),interactTypes);
                        popItemUtil.setOnPopItemClick(new PopItemUtil.PopItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                switch (position){
                                    case 0:
                                        Intent releasingNotice = new Intent(getContext(), ReleasingNoticesActivity.class);
                                        startActivity(releasingNotice);
                                        break;
                                    case 1:
                                        Intent gatherDocIntent = new Intent(getContext(), ReleasingDocGatheringActivity.class);
                                        startActivity(gatherDocIntent);
                                        break;
                                }
                            }
                        });
                    break;
                    case 1://互动
                        Intent startVoteIntent = new Intent(getContext(), StartVoteActivity.class);
                        startActivity(startVoteIntent);
                        break;
                    case 2://动态
                        Intent ReleasingNews = new Intent(getContext(), ReleasingNewsActivity.class);
                        startActivity(ReleasingNews);
                        break;
                }
            }
        });
    }

    @Override
    public void initData() {

    }


    //拍照并保存
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null){
            showToast("获取图片失败");
            return;
        }
        ArrayList<String> mImgs = new ArrayList<>();
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ImageLoderUtil.CAPTURE_PIICTURE_RESULT_CODE){
            mImgs=data.getStringArrayListExtra("selectImg");
        }else if(requestCode == ImageLoderUtil.CAPTURE_PHOTO_RESULT_CODE){
            if (resultCode == Activity.RESULT_OK) {
                String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
                FileOutputStream b = null;
                File file = new File("/sdcard/myImage/");
                file.mkdirs();// 创建文件夹
                String fileName = "/sdcard/myImage/"+name;
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
                mImgs.add(fileName);
            }
        }
        Intent intent = new Intent(getContext(),UploadPhotoActivity.class);
        intent.putStringArrayListExtra("imgsPath",mImgs);
        getContext().startActivity(intent);
    }
}
