package com.example.a731.aclass.activity;

import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.News;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.util.DateUtil;

import cn.bmob.v3.BmobUser;

import static com.example.a731.aclass.R.menu.menu_releasing_news;

/**
 * Created by Administrator on 2017/11/5/005.
 */

public class ReleasingNewsActivity extends BaseActivity{

    private Toolbar toolbar;
    private EditText tvContent;
    private Button btnAddPicture;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_releasing_news;
    }

    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.releasing_news_toolbar);
        tvContent = (EditText) findViewById(R.id.releasing_news_tv_content);
        btnAddPicture = (Button) findViewById(R.id.releasing_news_btn_add_picture);
        initToolbar();
        initData();
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
        news.setLike(0);
        //news.setPhotoList();

        if (content.equals("")){
            showToast("先填写好内容再发布吧~");
            return;
        }else{
            showToast("发布成功");
            //将news上传网上;
        }
    }

    @Override
    public void initListener() {
        //选择图片
        btnAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void initData() {

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
}
