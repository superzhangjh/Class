package com.example.a731.aclass.activity;

import android.app.Activity;
import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.FileInfoAdapter;
import com.example.a731.aclass.adapter.PhotoAdapter;
import com.example.a731.aclass.data.News;
import com.example.a731.aclass.data.Notice;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.util.DateUtil;
import com.example.a731.aclass.util.ImageLoderUtil;
import com.example.a731.aclass.util.PopItemUtil;
import com.example.a731.aclass.util.SharedPreferencesUtil;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/11/8/008.
 */

public class ReleasingDocGatheringActivity extends BaseActivity {


    private final int REQUESTCODE_FROM_ACTIVITY = 1000;

    private Toolbar toolbar;
    private EditText edtContent;
    private Button btnAddPicture;
    private Button btnAddFile;
    private RecyclerView recyclerPinture;
    private TextView tvFileCount;
    private RecyclerView recyclerFile;

    private Notice notice = new Notice();
    private List<String> pathList = new ArrayList<>(); //文件本地图片列表
    private List<File> fileList;//文件网络链接列表

    private PhotoAdapter photoAdapter;
    private FileInfoAdapter fileAdapter;
    private List<String> photoList;
    private String presentGroupId;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_doc_gathering;
    }

    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.releasing_doc_gather_toolbar);
        edtContent = (EditText) findViewById(R.id.releasing_doc_gather_edt_content);
        btnAddPicture = (Button) findViewById(R.id.releasing_doc_gather_btn_add_picture);
        btnAddFile = (Button) findViewById(R.id.releasing_doc_gather_btn_add_file);
        recyclerPinture = (RecyclerView) findViewById(R.id.releasing_news_recycler_pinture);
        tvFileCount = (TextView) findViewById(R.id.releasing_doc_gather_tv_file_count);
        recyclerFile = (RecyclerView) findViewById(R.id.releasing_doc_gather_recycler_file);
        initToolbar();
        initData();
        initPhotoRecyclerView();
        initFileRecyclerView();

    }

    private void initFileRecyclerView() {
        fileAdapter = new FileInfoAdapter(this,pathList);
        recyclerFile.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerFile.setAdapter(fileAdapter);
    }

    private void initPhotoRecyclerView() {
        photoAdapter = new PhotoAdapter(this,photoList);
        recyclerPinture.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerPinture.setAdapter(photoAdapter);
    }

    private void initToolbar() {
        toolbar.setTitle("资料收取");
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

    //发布通知
    private void pushNotice() {
        Users users = BmobUser.getCurrentUser(Users.class);
        notice = new Notice();
        notice.setCreator(users);
        String content = edtContent.getText().toString();
        notice.setContent(content);
        notice.setDate(DateUtil.yyyyMMdd_hhmmss());

        if (content.equals("")){
            showToast("先填写好内容再发布吧~");
            return;
        }else{
            showProgress("努力发布中...");
            //TODO:将fileList上传到网上获得链接，并把反馈得到的链接链接赋给设置给notice，然后上传notice到网络
            //TODO:notice.setFileList(new ArrayList<String>());
            //presenter.saveNews(presentGroupId,news,photoList);
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
        btnAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LFilePicker()
                        .withActivity(ReleasingDocGatheringActivity.this)
                        .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                        .start();
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
                pushNotice();
                break;
        }
        return true;
    }

    private void showPhotoDialog() {
        String[] strings = {"拍照","从图库选择"};
        new PopItemUtil(ReleasingDocGatheringActivity.this,strings)
                .setOnPopItemClick(new PopItemUtil.PopItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position){
                    case 0:
                        String state = Environment.getExternalStorageState();
                        if (state.equals(Environment.MEDIA_MOUNTED)) {
                            Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(getImageByCamera, ImageLoderUtil.CAPTURE_PHOTO_RESULT_CODE);
                        }
                        else {
                            showToast("请确认已经插入SD卡");
                        }
                        break;
                    case 1:
                        Intent intent = new Intent(ReleasingDocGatheringActivity.this,ImagePickerActivity.class);
                        startActivityForResult(intent,ImageLoderUtil.CAPTURE_PIICTURE_RESULT_CODE);
                        break;
                }
            }
        });
    }

    //拍照并保存
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<String> mImgs = new ArrayList<>();
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case ImageLoderUtil.CAPTURE_PIICTURE_RESULT_CODE:
                    mImgs=data.getStringArrayListExtra("selectImg");
                    photoList = mImgs;
                    photoAdapter.setOndataChange(photoList);
                    break;
                case ImageLoderUtil.CAPTURE_PHOTO_RESULT_CODE:
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
                    photoList= mImgs;
                    photoAdapter.setOndataChange(photoList);
                    break;

            /*---------------------------------------文件选择-------------------------------------------------*/
            case REQUESTCODE_FROM_ACTIVITY:
                pathList = data.getStringArrayListExtra(Constant.RESULT_INFO);
                fileAdapter.setOnDataChange(pathList);
                fileList = new ArrayList<>();
                for (String filename:pathList){
                    File file1 = new File(filename);
                    fileList.add(file1);
                }
                if (pathList.size()>0){
                    tvFileCount.setVisibility(View.VISIBLE);
                    tvFileCount.setText("已选择的文件("+pathList.size()+")");
                }else{
                    tvFileCount.setVisibility(View.GONE);
                }
                break;
            }
        }
    }

}
