package com.example.a731.aclass.util;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.a731.aclass.R;

/**
 * Created by Administrator on 2017/11/8/008.
 */

public class PopItemUtil {

    private PopItemClickListener popItemClickListener;

    public void setOnPopItemClick(PopItemClickListener listener) {
        this.popItemClickListener = listener;
    }

    public void onClick(View v, int position) {
        if (popItemClickListener!=null){
            popItemClickListener.onItemClick(v,position);
            InputMethodManager imm1 = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm1.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public  interface PopItemClickListener{
        void onItemClick(View view,int position);
    }

    public PopItemUtil(final Context context, String[] data) {

        //TODO:根据评论时间先后排序

        //加载弹出框的布局
        View contentView = LayoutInflater.from(context).inflate(
                R.layout.pop_item_window, null);

        //初始化适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,R.layout.list_item,data);
        ListView listView = (ListView) contentView.findViewById(R.id.pop_item_window_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //键盘关闭
                onClick(view,position);
            }
        });

        //设置弹出框的宽度和高度
        final PopupWindow popupWindow = new PopupWindow(contentView,
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
        //弹出页面
        popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);

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
    }
}
