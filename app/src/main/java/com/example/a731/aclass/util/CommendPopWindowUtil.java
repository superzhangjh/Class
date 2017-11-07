package com.example.a731.aclass.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.CommendAdapter;
import com.example.a731.aclass.data.Commend;
import com.example.a731.aclass.data.Users;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/11/7/007.
 */

public class CommendPopWindowUtil {

    private static PopupWindow popupWindow;
    private static View contentView;
    private static List<Commend> commendList;
    private static CommendAdapter adapter;

    public static void getCommendDataFromBmob() {
        //TODO:从网络获取评论,根据type获取评论的类型(个人、投票、动态)
        commendList = new ArrayList<>();
    }

    //外部获取评论列表
    public static List<Commend> getCommendList() {
        getCommendDataFromBmob();//先同步网络数据
        return commendList;
    }

    //从底部显示
    public static void openPopWindow() {
        popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
    }

    public static void createPopwindow(Context context,int type) {

        getCommendDataFromBmob();

        //TODO:根据评论时间先后排序

        //加载弹出框的布局
        contentView = LayoutInflater.from(context).inflate(
                R.layout.commend_pop_window, null);
        //绑定控件
        ImageButton btnCross = (ImageButton) contentView.findViewById(R.id.pop_window_commend_cross);
        ;
        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.pop_window_commend_recycler_view);
        final EditText edtContent = (EditText) contentView.findViewById(R.id.pop_window_commend_edt_content);
        TextView btnSend = (TextView) contentView.findViewById(R.id.pop_window_commend_tv_send);

        //初始化适配器
        adapter = new CommendAdapter(context, commendList);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        //设置弹出框的宽度和高度
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);
        //进入退出的动画
        popupWindow.setAnimationStyle(R.style.anim_popup_dir);

        // 按下android回退物理键 PopipWindow消失解决
        contentView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                        return true;
                    }
                }
                return false;
            }
        });
        //点击的监听
        btnCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edtContent.getText().toString().trim();
                if (!content.equals("")){
                    Commend commend = new Commend();
                    Users users = BmobUser.getCurrentUser(Users.class);
                    commend.setUsers(users);
                    commend.setLikeList(new ArrayList<String>());
                    String date = DateUtil.yyyyMMdd_hhmmss();
                    commend.setDate(date);
                    commend.setContent(content);
                    commendList.add(commend);
                    adapter.setOnDataChanged(commendList);
                    edtContent.getText().clear();

                    //TODO:存储评论到网络
                }
            }
        });
    }
}
