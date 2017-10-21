package com.example.a731.aclass.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.ChatAdapter;
import com.example.a731.aclass.adapter.EMMessageListenerAdapter;
import com.example.a731.aclass.data.Mess;
import com.example.a731.aclass.presenter.ChatPresenter;
import com.example.a731.aclass.presenter.impl.ChatPresenterImpl;
import com.example.a731.aclass.view.ChatView;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/18/018.
 */

public class ChatActivity extends BaseActivity implements ChatView {
    private Toolbar toolbar;
    private TextView chatName;
    private RecyclerView recyclerView;
    private EditText edtInput;
    private ImageButton imgFn;//功能按钮
    private ImageButton imgInfo;//详情按钮
    private SwipeLayout swipeLayout;

    private ChatAdapter adapter;
    private ChatPresenter chatPresenter = new ChatPresenterImpl(this);
    private String username;

    private EMMessageListenerAdapter msgListener;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_chat;
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        initToolbar();

        //测试内容
        initSwipeLayout();
        initRecyclerview();
        initEditText();
    }

    private void initEditText() {
        edtInput = (EditText) findViewById(R.id.chat_edt_fn);
        imgFn = (ImageButton) findViewById(R.id.chat_ib_fn);
        imgInfo = (ImageButton) findViewById(R.id.chat_ib_info);

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
                }else{
                    sendMessage();
                }

            }
        });

        imgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //根据接收的布尔值判断是否为圈消息，如果是则跳转到圈详情页面，如果不是则跳转到个人详情页面
                boolean isGroupMess = getIntent().getBooleanExtra("isGroupMess",false);
                if (!isGroupMess){
                    //个人详情页面
                    Intent intent = new Intent(getApplicationContext(),UserInfoActivity.class);
                    startActivity(intent);
                }else{
                    //圈详情页面
                    Intent intent = new Intent(getApplicationContext(),GroupInfoActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void sendMessage() {
        //发送文本消息
        String edtContent = edtInput.getText().toString();
        chatPresenter.senMessage(username,edtContent);
        edtInput.getText().clear();
    }

    //消息内容呈现的recyclerview
    private void initRecyclerview() {
        recyclerView = (RecyclerView) findViewById(R.id.chat_recycle_view);
        adapter = new ChatAdapter(this,chatPresenter.getMessage(),username);
        LinearLayoutManager manager=new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
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

        msgListener = new EMMessageListenerAdapter() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                EMMessage msg = messages.get(0);
                Mess mess = new Mess();
                mess.setCreatorID(msg.getFrom());
                mess.setDate(msg.getMsgTime()+"");
                switch(msg.getType()){
                    //文本信息
                    case TXT:
                        EMTextMessageBody body = (EMTextMessageBody) msg.getBody();
                        mess.setMessage(body.getMessage());
                        showToast("收到信息"+body.getMessage());
                        adapter.add(mess);
                        smoothScrollToBottom();
                        break;
                    //图片信息
                    case IMAGE:
                        break;
                    //视频
                    case VIDEO:
                        break;
                    //位置
                    case LOCATION:
                        break;
                    //声音
                    case VOICE:
                        break;
                    //文件
                    case FILE:
                        break;
                }
                smoothScrollToBottom();
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    @Override
    public void initData() {
        chatPresenter.getConversationRecord(username);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }

    @Override
    public void onSendMessageSuccess() {
        updateList();
    }

    private void updateList() {
        showToast("更新会话记录成功");
        adapter.notifyDataSetChanged();
        smoothScrollToBottom();
    }

    @Override
    public void onSendMessageFail(String msg) {
        showToast("发送消息失败"+msg);

    }


    //获取会话记录
    @Override
    public void onGetRecordSuccess() {
        showToast("获取会话记录成功");
        updateList();
    }

    private void smoothScrollToBottom() {
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }
}
