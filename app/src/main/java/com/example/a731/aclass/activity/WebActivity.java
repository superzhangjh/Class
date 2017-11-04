package com.example.a731.aclass.activity;

import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.a731.aclass.R;

/**
 * Created by Administrator on 2017/11/4/004.
 */

public class WebActivity extends BaseActivity {

    private Toolbar toolbar;
    private WebView webview;
    private String url;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_web;
    }

    @Override
    public void initView() {
        //初始化url
        url = getIntent().getStringExtra("url");

        toolbar = (Toolbar) findViewById(R.id.activity_web_toolbar);
        webview = (WebView) findViewById(R.id.activity_web_webview);

        toolbar.setTitle(url);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initListener() {

    }

    //点击返回时后退网页
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void initData() {
        //打开页面时 自适应屏幕
        WebSettings webSettings = webview.getSettings();
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);

        //页面支持缩放
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);

        //如果webView中需要用户手动输入用户名、密码或其他，则webview必须设置支持获取手势焦点。
        webview.requestFocusFromTouch();

        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(url);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        getMenuInflater().inflate(R.menu.web_menu,menu);
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_web_copy:
                //TODO:复制链接url
                break;
            case R.id.menu_web_new_client:
                //使用其他浏览器打开
                break;
            default:break;
        }
        return  true;
    }
}
