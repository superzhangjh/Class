package com.example.a731.aclass.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.SysNotification;
import com.example.a731.aclass.presenter.SystematicNotificationPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郑选辉 on 2017/10/20.
 */

public class SystematicNotificationAdapter extends BaseAdapter{

    private Context context;
    private List<SysNotification> notifications;
    private SystematicNotificationPresenter presenter;

    private static final String TAG = "NotificationAdapter";

    private LinearLayout content;
    private TextView tvMessage;
    private Button btnAccept,btnRefuse;

    public SystematicNotificationAdapter(Context context,SystematicNotificationPresenter presenter,List<SysNotification> notifications){
        this.context = context;
        this.presenter = presenter;
        this.notifications = notifications;
    }

    @Override
    public int getCount() {
        return notifications == null?0:notifications.size();
    }

    @Override
    public Object getItem(int position) {
        return notifications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void onDataChanged(SysNotification notification){
        notifications.add(notification);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_systematic_notification_item,parent,false);
        }
        final SysNotification notification = (SysNotification) getItem(position);

        content = (LinearLayout) convertView.findViewById(R.id.systematic_notification_item_ll_btnGroup);
        tvMessage = (TextView) convertView.findViewById(R.id.systematic_notification_item_tv_mess);
        btnAccept = (Button) convertView.findViewById(R.id.systematic_notification_item_btn_accept);
        btnRefuse = (Button) convertView.findViewById(R.id.systematic_notification_item_btn_refuse);

        tvMessage.setText(notification.getMessage());

        if (notification.getStatue() == SysNotification.NEED_DEAL){
            content.setVisibility(View.VISIBLE);
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notification.setStatue(SysNotification.ACCEPTED);
                    switch (notification.getType()){
                        case SysNotification.APPLY_FRIEND:
                            Log.i(TAG,"开始同意好友申请");
                            presenter.acceptApplyFriend(notification.getUsername());
                            break;
                        case SysNotification.APPLY_JOIN_GROUP:
                            Log.i(TAG,"开始同意班圈申请");
                            presenter.acceptApplyJoinGroup(notification.getUsername(),notification.getGroupId());
                            break;
                    }
                }
            });
            btnRefuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notification.setStatue(SysNotification.REFUSED);
                    switch (notification.getType()){
                        case SysNotification.APPLY_FRIEND:
                            Log.i(TAG,"开始拒绝好友申请");
                            presenter.refuseApplyFriend(notification.getUsername());
                            break;
                        case SysNotification.APPLY_JOIN_GROUP:
                            Log.i(TAG,"开始拒绝班圈申请");
                            presenter.refuseApplyJoinGroup(notification.getUsername(),notification.getGroupId());
                            break;
                    }
                    notification.save();
                }
            });
        }
        return convertView;
    }
}
