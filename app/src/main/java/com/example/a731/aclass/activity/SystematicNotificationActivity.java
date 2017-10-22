package com.example.a731.aclass.activity;

import android.widget.ListView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.EMContactListenerAdapter;
import com.example.a731.aclass.adapter.EMGroupChangeListenerAdapter;
import com.example.a731.aclass.adapter.SystematicNotificationAdapter;
import com.example.a731.aclass.data.SysNotification;
import com.example.a731.aclass.presenter.SystematicNotificationPresenter;
import com.example.a731.aclass.presenter.impl.SystematicNotificationPresenterImpl;
import com.example.a731.aclass.view.SystematicNotificationView;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/10/20.
 */

public class SystematicNotificationActivity extends BaseActivity implements SystematicNotificationView{

    private ListView listOfNotification;
    private List<SysNotification> notifications;
    private SystematicNotificationAdapter adapter;
    private SystematicNotificationPresenter presenter = new SystematicNotificationPresenterImpl(this);

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_systematic_notification;
    }

    @Override
    public void initView() {
        listOfNotification = (ListView) findViewById(R.id.systematic_notification_list);

    }

    private void iniListView() {
        adapter = new SystematicNotificationAdapter(this,presenter,notifications);
        listOfNotification.setAdapter(adapter);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        notifications = DataSupport.findAll(SysNotification.class);
        if (adapter==null){
            iniListView();
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAcceptFriendFail(String message) {
        showToast("同意好友申请失败"+message);
    }

    @Override
    public void refresh() {
        showToast("你点击了同意");
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAcceptApplyJoinGroupFail(String message) {
        showToast("同意班圈申请失败"+message);
    }


    @Override
    public void onRefuseApplyFriendFail(String message) {
        showToast("拒绝好友申请失败"+message);
    }

    @Override
    public void onRefuseApplyJoinGroupFail(String message) {
        showToast("拒绝班圈申请失败"+message);
    }
}
