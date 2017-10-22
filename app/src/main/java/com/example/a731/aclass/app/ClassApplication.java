package com.example.a731.aclass.app;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.a731.aclass.R;
import com.example.a731.aclass.activity.SystematicNotificationActivity;
import com.example.a731.aclass.adapter.EMContactListenerAdapter;
import com.example.a731.aclass.adapter.EMGroupChangeListenerAdapter;
import com.example.a731.aclass.data.SysNotification;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

import org.litepal.LitePalApplication;
import org.litepal.crud.DataSupport;

import java.util.Iterator;
import java.util.List;

import cn.bmob.v3.Bmob;

/**
 * Created by 郑选辉 on 2017/10/4.
 */

public class ClassApplication extends LitePalApplication {

    public static final String TAG = "ClassApplication";
    private EMGroupChangeListenerAdapter groupListener;
    private EMContactListener listener;

    @Override
    public void onCreate() {
        super.onCreate();
        initEaseMob();
        initBmob();
        initEaseMobListener();

    }

    private void initEaseMobListener() {
        groupListener = new EMGroupChangeListenerAdapter() {
            @Override
            public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
                //接收到群组加入邀请
                String message = inviter+" 邀请你加入 "+groupName+" 班圈";
                    Log.i(TAG,"开始储存数据");
                    SysNotification notification = new SysNotification();
                    notification.setMessage(inviter+" 邀请你加入 "+groupName+" 班圈");
                    notification.setStatue(SysNotification.NEED_DEAL);
                    notification.setGroupId(groupId);
                    notification.setType(SysNotification.INVITED_GROUP);
                    notification.save();
                showNotification(message);
            }

            @Override
            public void onRequestToJoinReceived(String groupId, String groupName, String applyer, String reason) {
                //用户申请加入群
                String message = applyer+" 申请加入 "+groupName+" 班圈";
                    Log.i(TAG,"开始储存数据");
                    SysNotification notification = new SysNotification();
                    notification.setMessage(applyer+" 申请加入 "+groupName+" 班圈");
                    notification.setGroupId(groupId);
                    notification.setUsername(applyer);
                    notification.setStatue(SysNotification.NEED_DEAL);
                    notification.setType(SysNotification.APPLY_JOIN_GROUP);
                    notification.save();
                showNotification(message);
            }
        };
        EMClient.getInstance().groupManager().addGroupChangeListener(groupListener);

        listener = new EMContactListenerAdapter() {
            @Override
            public void onContactInvited(String username, String reason) {
                //收到好友邀请
                SysNotification notification = new SysNotification();
                String message = username+"邀请加你为好友";

                    Log.i(TAG,"开始储存数据");
                    notification.setMessage(message);
                    notification.setStatue(SysNotification.NEED_DEAL);
                    notification.setUsername(username);
                    notification.setType(SysNotification.APPLY_FRIEND);
                    notification.save();

                showNotification(message);
            }
        };
        EMClient.getInstance().contactManager().setContactListener(listener);
    }

    private void initBmob() {
        Bmob.initialize(this, "2e729391a3a055624f6275df73b7ade5");
    }

    private void initEaseMob() {
        EMOptions options = new EMOptions();
// 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回
        if (processAppName == null ||!processAppName.equalsIgnoreCase(getPackageName())) {
            Log.e(TAG, "enter the service process!");
            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

//初始化
        EMClient.getInstance().init(getApplicationContext(), options);
    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    private void showNotification(String message) {
        String contentText = message;
        Intent notificationActivity = new Intent(this, SystematicNotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, notificationActivity, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("你有新的通知")
                .setContentText(contentText)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(1, notification);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        EMClient.getInstance().groupManager().removeGroupChangeListener(groupListener);
        EMClient.getInstance().contactManager().removeContactListener(listener);
    }
}
