package com.example.a731.aclass.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.a731.aclass.util.ToastUtil;

/**
 * Created by yls on 2017/6/27.
 */

public abstract class BaseFragment extends Fragment {

    /** Fragment要显示的界面 */
    protected View mRootView;
    protected Activity mActivity;

    // 创建Fragment要显示的界面（视图）
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mActivity = getActivity();

        if (mRootView == null) {
            // 把一个布局文件转换成View对象
            // View.inflate()
            // 参数3： false, 表示布局不会作为子控件，添加到container中，
            mRootView = LayoutInflater.from(getContext()).inflate(
                    getLayoutRes(),container,false);

            initView();
            initListener();
            initData();
        }

        return mRootView;
    }

    /** 返回Fragment界面的布局文件 */
    protected abstract int getLayoutRes();

    /** 查找子控件 */
    public abstract void initView();

    /** 设置监听器 */
    public abstract void initListener() ;

    /** 初始化数据 */
    public abstract void initData();


    public void showToast(String msg) {
        ToastUtil.showToast(getContext().getApplicationContext(),msg);
    }
}