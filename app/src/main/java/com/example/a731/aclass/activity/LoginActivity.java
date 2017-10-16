package com.example.a731.aclass.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a731.aclass.R;
import com.example.a731.aclass.presenter.LoginPresenter;
import com.example.a731.aclass.presenter.impl.LoginPresenterImpl;
import com.example.a731.aclass.view.LoginView;

/**
 * Created by 郑选辉 on 2017/9/19.
 */

public class LoginActivity extends BaseActivity implements LoginView{

    private EditText edt_phoneNum;
    private EditText edt_password;
    private Button btn_signIn;
    private TextView tv_signUp;
    private LoginPresenter mPresenter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        edt_phoneNum = (EditText) findViewById(R.id.login_edt_phoneNum);
        edt_password = (EditText) findViewById(R.id.login_edt_password);
        btn_signIn = (Button) findViewById(R.id.login_btn_sign_in);
        tv_signUp = (TextView) findViewById(R.id.login_tv_sign_up);

        mPresenter=new LoginPresenterImpl(this);
    }

    @Override
    public void initListener() {
        btn_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = edt_phoneNum.getText().toString();
                String password = edt_password.getText().toString();
                mPresenter.onCheckLogin(phoneNum,password);
                showProgress("正在登陆");
            }
        });

        tv_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
    }

    @Override
    public void onLoginSuccess() {
        showToast("登录成功");
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
        hideProgress();
        finish();
    }
    @Override
    public void onLoginFail(String msg) {
        hideProgress();
        showToast("登录失败"+msg);
    }
}
