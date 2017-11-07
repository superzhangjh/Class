package com.example.a731.aclass.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.PhotoAdapter;
import com.example.a731.aclass.data.News;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.ReleasingNewsPresenter;
import com.example.a731.aclass.presenter.impl.ReleasingNewsPresenterImpl;
import com.example.a731.aclass.util.DateUtil;
import com.example.a731.aclass.util.ImageLoderUtil;
import com.example.a731.aclass.util.SharedPreferencesUtil;
import com.example.a731.aclass.view.ReleasingNewsView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobUser;

import static com.example.a731.aclass.R.menu.menu_releasing_news;

/**
 * Created by Administrator on 2017/11/5/005.
 */

public class ReleasingNewsActivity extends BaseActivity implements ReleasingNewsView{

    private Toolbar toolbar;
    private EditText tvContent;
    private Button btnAddPicture;
    private RecyclerView recyclerPhoto;
    private PhotoAdapter photoAdapter;
    private List<String> photoList;
    private String presentGroupId;
    private ReleasingNewsPresenter presenter = new ReleasingNewsPresenterImpl(this);

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_releasing_news;
    }

    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.releasing_news_toolbar);
        tvContent = (EditText) findViewById(R.id.releasing_news_tv_content);
        btnAddPicture = (Button) findViewById(R.id.releasing_news_btn_add_picture);
        recyclerPhoto = (RecyclerView) findViewById(R.id.releasing_news_recycler_view);
        initToolbar();
        initData();
        initPhotoRecyclerView();
    }

    private void initPhotoRecyclerView() {
        photoAdapter = new PhotoAdapter(this,photoList);
        recyclerPhoto.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerPhoto.setAdapter(photoAdapter);
    }

    private void initToolbar() {
        toolbar.setTitle("发布动态");
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

    //发布动态
    private void pushNews() {
        Users users = BmobUser.getCurrentUser(Users.class);
        News news = new News();
        news.setUsers(users);
        String content = tvContent.getText().toString();
        news.setContent(content);
        news.setDate(DateUtil.yyyyMMdd_hhmmss());
        news.setLike(new ArrayList<String>());

        if (content.equals("")){
            showToast("先填写好内容再发布吧~");
            return;
        }else{
            showProgress("努力发布中...");
            presenter.saveNews(presentGroupId,news,photoList);
        }
    }

    @Override
    public void initListener() {
        //选择图片
        btnAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPhotoDialog();
            }
        });
    }

    @Override
    public void initData() {
        presentGroupId = SharedPreferencesUtil.lodaDataFromSharedPreferences(BmobUser.getCurrentUser().getUsername(),this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_releasing_news,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_releasing_news_push:
                pushNews();
                break;
        }
        return true;
    }

    private void showPhotoDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(ReleasingNewsActivity.this).create();//创建一个AlertDialog对象
        dialog.show();//一定要先show出来再设置dialog的参数，不然就不会改变dialog的大小了\
        Window window = dialog.getWindow();
        window.setContentView(R.layout.dialog_select_photo);
        window.setGravity(Gravity.CENTER);//设置对话框在界面底部显示
        dialog.setCanceledOnTouchOutside(true);
        TextView tvTakePhoto = (TextView) window.findViewById(R.id.dialog_select_photo_take);
        TextView tvSelectPhoto = (TextView) window.findViewById(R.id.dialog_select_photo_album);

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
                Intent intent = new Intent(ReleasingNewsActivity.this,ImagePickerActivity.class);
                startActivityForResult(intent,ImageLoderUtil.CAPTURE_PIICTURE_RESULT_CODE);
                dialog.cancel();
            }
        });
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
                File file = new File("/sdcard/group_circle/");
                file.mkdirs();// 创建文件夹
                String fileName = "/sdcard/group_circle/"+name;
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
                photoList = mImgs;
                photoAdapter.setOndataChange(photoList);
            }
        }
        photoList = mImgs;
        photoAdapter.setOndataChange(photoList);
    }

    @Override
    public void onSaveNoticeSuccess() {
        showToast("发布成功！");
        hideProgress();
        finish();
    }

    @Override
    public void onSaveNoticeFail(String message) {
        hideProgress();
        showToast("保存数据失败:"+message);
    }
}
