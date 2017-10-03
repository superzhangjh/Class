package com.example.a731.aclass.fragment;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.azhon.suspensionfab.FabAttributes;
import com.azhon.suspensionfab.OnFabClickListener;
import com.azhon.suspensionfab.SuspensionFab;
import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.CircleFragmentPagerAdapter;
import com.example.a731.aclass.util.Animation.FabButtonAnimate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/15/015.
 */

public class CircleFragment extends BaseFragment{

    private TabLayout tablayout;
    private ViewPager viewpager;
    private CircleFragmentPagerAdapter adapter;
    private List<Fragment> list_fragment;
    private List<String> list_title;
    private SuspensionFab susFab;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_circle;
    }

    @Override
    public void initView() {
        list_fragment = new ArrayList<>();
        list_fragment.add(new Fragment());
        list_fragment.add(new Fragment());
        list_fragment.add(new Fragment());
        list_fragment.add(new Fragment());

        list_title = new ArrayList<>();
        list_title.add("动态");
        list_title.add("通知");
        list_title.add("相册");
        list_title.add("互动");
        initTabViewpager();
        initSuspensionFab();
    }

    //悬浮按钮
    private void initSuspensionFab() {
        susFab = (SuspensionFab) mRootView.findViewById(R.id.circle_suspensionfab);
        //构建展开按钮属性
        FabAttributes collection = new FabAttributes.Builder()
                .setBackgroundTint(Color.parseColor("#2096F3"))
                .setSrc(getResources().getDrawable(R.drawable.chat_fn))
                .setFabSize(FloatingActionButton.SIZE_AUTO)
                .setPressedTranslationZ(10)
                .setTag(1)
                .build();
        FabAttributes email = new FabAttributes.Builder()
                .setBackgroundTint(Color.parseColor("#FF9800"))
                .setSrc(getResources().getDrawable(R.drawable.chat_fn_send))
                .setFabSize(FloatingActionButton.SIZE_AUTO)
                .setPressedTranslationZ(10)
                .setTag(2)
                .build();
        FabAttributes news = new FabAttributes.Builder()
                .setBackgroundTint(Color.parseColor("#03A9F4"))
                .setSrc(getResources().getDrawable(R.drawable.chat_sound))
                .setFabSize(FloatingActionButton.SIZE_AUTO)
                .setPressedTranslationZ(10)
                .setTag(3)
                .build();
        //添加菜单
        susFab.addFab(collection,email,news);
        susFab.setAnimationManager(new FabButtonAnimate(susFab));
        susFab.setFabClickListener(new OnFabClickListener() {
            @Override
            public void onFabClick(FloatingActionButton fab, Object tag) {
                String msg = "";
                switch ((int)tag){
                    case 1:msg="第1个";break;
                    case 2:msg="第2个";break;
                    case 3:msg="第3个";break;
                    default:break;
                }
                showToast(msg);
            }
        });
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    private void initTabViewpager() {
        tablayout = (TabLayout) mRootView.findViewById(R.id.circle_tablayout);
        viewpager = (ViewPager) mRootView.findViewById(R.id.circle_viewpager);
        //设置tablayout显示模式
        tablayout.setTabMode(TabLayout.MODE_FIXED);

        adapter = new CircleFragmentPagerAdapter(getActivity().getSupportFragmentManager(),list_fragment,list_title);
        viewpager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewpager);
    }
}
