package com.example.a731.aclass.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.a731.aclass.R;

import java.util.List;

/**
 * Created by Administrator on 2017/9/21/021.
 */

public class CircleFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> list_fragment;                         //fragment列表
    private List<String> list_Title;                              //tab名的列表
    private Context context;


    public CircleFragmentPagerAdapter(Context context,FragmentManager fm, List<Fragment> list_fragment, List<String> list_Title) {
        super(fm);
        this.context = context;
        this.list_fragment = list_fragment;
        this.list_Title = list_Title;
    }

    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return list_Title.size();
    }

    //此方法用来显示tab上的名字
    @Override
    public CharSequence getPageTitle(int position) {
        return list_Title.get(position % list_Title.size());
    }

    //获取自定义布局
    public View getTabView(int position) {
        View tabView = LayoutInflater.from(context).inflate(R.layout.item_tab_layout, null);
        TextView tabTitle = (TextView) tabView.findViewById(R.id.tv_tab_title);
        tabTitle.setText(list_Title.get(position));
        return tabView;
    }
}
