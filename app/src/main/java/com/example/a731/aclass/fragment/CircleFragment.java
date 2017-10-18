package com.example.a731.aclass.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.azhon.suspensionfab.FabAttributes;
import com.azhon.suspensionfab.OnFabClickListener;
import com.azhon.suspensionfab.SuspensionFab;
import com.example.a731.aclass.R;
import com.example.a731.aclass.activity.UploadPhotoActivity;
import com.example.a731.aclass.adapter.CircleFragmentPagerAdapter;
import com.example.a731.aclass.util.Animation.FabButtonAnimate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/9/15/015.
 */

public class CircleFragment extends BaseFragment{

    private TabLayout tablayout;
    private ViewPager viewpager;
    private CircleFragmentPagerAdapter adapter;
    private List<Fragment> list_fragment;
    private List<String> list_title;
    private SuspensionFab susFab;//浮动按钮
    private TextView tvTakePhoto;
    private TextView tvSelectPhoto;
    private static final int REQUEST_CAMERA = 1001;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_circle;
    }

    @Override
    public void initView() {
        tvTakePhoto = (TextView) mRootView.findViewById(R.id.dialog_select_photo_take);
        tvSelectPhoto = (TextView) mRootView.findViewById(R.id.dialog_select_photo_album);

        list_fragment = new ArrayList<>();
        list_fragment.add(new Fragment());
        list_fragment.add(new CircleNoticeFragment());
        list_fragment.add(new Fragment());
        list_fragment.add(new CircleGalleryFragment());

        list_title = new ArrayList<>();
        list_title.add("动态");
        list_title.add("通知");
        list_title.add("互动");
        list_title.add("相册");
        initTabViewpager();
        initSuspensionFab();
    }

    //悬浮按钮
    private void initSuspensionFab() {
        susFab = (SuspensionFab) mRootView.findViewById(R.id.circle_suspensionfab);
        //构建展开按钮属性
        //更多
        FabAttributes more = new FabAttributes.Builder()
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
        susFab.addFab(more,album,notice);
        susFab.setAnimationManager(new FabButtonAnimate(susFab));
        //菜单点击事件
        susFab.setFabClickListener(new OnFabClickListener() {
            @Override
            public void onFabClick(FloatingActionButton fab, Object tag) {
                String msg = "";
                switch ((int)tag){
                    case 1:
                        msg="更多";
                        break;
                    case 2:
                        msg="上传照片";
                        showFhotoDialog();break;
                    case 3:msg="发布通知";break;
                    default:break;
                }
                showToast(msg);
            }
        });
    }

    private void showFhotoDialog() {
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
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
                }
                // 启动系统相机
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CAMERA);
                dialog.cancel();
            }
        });
        //选择照片
        tvSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.cancel();
            }
        });
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    private void initTabViewpager() {
        tablayout = (TabLayout) mRootView.findViewById(R.id.circle_tablayout);
        viewpager = (ViewPager) mRootView.findViewById(R.id.circle_viewpager);
        //设置tablayout显示模式
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        adapter = new CircleFragmentPagerAdapter(getActivity().getSupportFragmentManager(),list_fragment,list_title);
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
        tablayout.setSmoothScrollingEnabled(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //拍照上传
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == REQUEST_CAMERA){
                dialogTakePhotoMethod1(data);
            }
        }else{
            //获取失败
        }
    }

    //拍照并保存
    private void dialogTakePhotoMethod1(Intent data){
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)){
            Log.i("SDcardStatus:","SD card is not avaiable/writeable right now.");
            return;
        }
        //以时间为文件命名
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = format.format(new Date())+".jpg";
        Log.i("---fileName:",fileName);

        //将intent返回的数据转换为bitmap
        Bundle bundle = data.getExtras();
        Bitmap bitmap = (Bitmap) bundle.get("data");
        Log.i("---bitmap:",bitmap.toString());

        //将bitmap存入本地
        String dir = "/sdcard/Class/";//文件夹名字
        File file = new File(dir);//文件夹名字
        file.mkdirs();//创建文件夹
        fileName = dir + fileName;//文件存放名字
        try {
            FileOutputStream out = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);// 把数据写入文件
            out.flush();
            out.close();
            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(getContext(),UploadPhotoActivity.class);
        intent.putExtra("path",fileName);
        getContext().startActivity(intent);
    }
}
