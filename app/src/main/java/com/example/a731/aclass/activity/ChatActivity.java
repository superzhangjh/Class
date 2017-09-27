package com.example.a731.aclass.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.ChatAdapter;
import com.example.a731.aclass.adapter.MessAdapter;
import com.example.a731.aclass.data.Mess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/18/018.
 */

public class ChatActivity extends BaseActivity{
    private Toolbar toolbar;
    private TextView chatName;
    private RecyclerView recyclerView;
    private List<Mess> messes;
    private EditText edtInput;
    private ImageView imgFn;
    private SwipeLayout swipeLayout;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_chat;
    }

    @Override
    public void initView() {
        initToolbar();

        //测试内容
        initTestData();
        initSwipeLayout();
        initRecyclerview();
        initEditText();
    }

    private void initEditText() {
        edtInput = (EditText) findViewById(R.id.chat_edt_fn);
        imgFn = (ImageView) findViewById(R.id.chat_ib_fn);


        imgFn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edtContent = edtInput.getText().toString().trim();
                if (edtContent.length()==0){
                    //关闭当前软键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(edtInput.getWindowToken(), 0) ;
                    //弹出滑动框
                    swipeLayout.open(SwipeLayout.DragEdge.Bottom);
                }
            }
        });
    }


    private void initRecyclerview() {
        recyclerView = (RecyclerView) findViewById(R.id.chat_recycle_view);
        ChatAdapter adapter = new ChatAdapter(this,messes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void initListener() {
        edtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //监听右侧功能按钮变化:输入时变化成发送
                String content = edtInput.getText().toString().trim();
                if (content.length()!=0){
                    imgFn.setBackgroundResource(R.drawable.chat_fn_send);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //监听右侧功能按钮变化:完成时变成FN按键
                String content = edtInput.getText().toString().trim();
                if (content.length()==0){
                    imgFn.setBackgroundResource(R.drawable.chat_fn);
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    private void initToolbar() {
        //设置toolbar
        toolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        chatName = (TextView) findViewById(R.id.chat_tv_name);
        setSupportActionBar(toolbar);

        // 显示标题栏左上角的返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //显示对方名称
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        chatName.setText(name);
        //点击toolbar后导航栏 左上角的图标后，退出当前界面
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void initSwipeLayout() {
        swipeLayout = (SwipeLayout) findViewById(R.id.chat_swipelayout);
        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Bottom,this.findViewById(R.id.chat_swipelayout_bottom));
    }

    private void initTestData() {
        messes = new ArrayList<>();

        String message = "我是消息！";
        for (int i=0;i<10;i++){
            Mess mess = new Mess();
            mess.setMessage("我是第"+(i+1)+"条消息," + message);
            message = message + message;
            mess.setDate("2017-09-20 10:20");
            messes.add(mess);
        }
    }


}
