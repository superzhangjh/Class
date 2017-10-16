package com.example.a731.aclass.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.EMMessageListenerAdapter;
import com.example.a731.aclass.adapter.MessAdapter;
import com.example.a731.aclass.data.Conversation;
import com.example.a731.aclass.data.Mess;
import com.example.a731.aclass.presenter.MessPresenter;
import com.example.a731.aclass.presenter.impl.MessPresenterImpl;
import com.example.a731.aclass.util.EaseMobUtil;
import com.example.a731.aclass.view.MessView;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/15/015.
 */

public class MessFragment extends BaseFragment implements MessView{

    private RecyclerView recyclerView;

    private MessAdapter adapter;

    private MessPresenter messPresenter = new MessPresenterImpl(this);

    private EMMessageListener msgListener;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_mess;
    }

    @Override
    public void initView() {
        initRecyclerview();
    }

    @Override
    public void initListener() {
        msgListener = new EMMessageListenerAdapter() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                //收到消息
                messPresenter.getConversations();
            }
        };
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    @Override
    public void initData() {
        messPresenter.getConversations();
    }

    private void initRecyclerview() {
        recyclerView = (RecyclerView) mRootView.findViewById(R.id.mess_recycle_view);
        adapter = new MessAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onGetConversationSuccess(List<Conversation> conversations) {
        showToast("数据更新成功");
        adapter.onDataChanged(conversations);
    }

    @Override
    public void onGetConversationFail() {

    }

    @Override
    public void onResume() {
        super.onResume();
        messPresenter.getConversations();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }
}
