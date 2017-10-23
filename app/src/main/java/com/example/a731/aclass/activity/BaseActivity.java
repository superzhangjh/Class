package com.example.a731.aclass.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.a731.aclass.util.ToastUtil;

/**
 * Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());


        initView();
        initData();
        initListener();
    }

    /** 返回Activity界面的布局文件 */
    protected abstract int getLayoutRes();

    /** 查找子控件 */
    public abstract void initView();

    /** 设置监听器 */
    public abstract void initListener() ;

    /** 初始化数据 */
    public abstract void initData();

    public void showToast(String msg) {
        ToastUtil.showToast(getApplicationContext(),msg);
    }

    protected void showProgress(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(true);
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();
    }

    protected void hideProgress() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

}
