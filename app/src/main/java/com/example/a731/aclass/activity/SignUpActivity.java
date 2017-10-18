package com.example.a731.aclass.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.a731.aclass.R;
import com.example.a731.aclass.presenter.SignUpPresenter;
import com.example.a731.aclass.presenter.impl.SignUpPresenterImpl;
import com.example.a731.aclass.view.SignUpView;

/**
 * Created by 郑选辉 on 2017/9/19.
 */

public class SignUpActivity extends BaseActivity implements SignUpView {

    private EditText edt_phoneNum;
    private EditText edt_password;
    private EditText edt_vfCode;
    private EditText edt_userName;
    private Button btn_getCode;
    private Button btn_signUp;
    private SignUpPresenter signUpPresenter;


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_sign_up;
    }

    @Override
    public void initView() {
        edt_phoneNum = (EditText) findViewById(R.id.sign_up_edt_phoneNum);
        edt_password = (EditText) findViewById(R.id.sign_up_edt_password);
        edt_vfCode = (EditText) findViewById(R.id.sign_up_edt_vf_code);
        edt_userName = (EditText) findViewById(R.id.sign_up_edt_userName);
        btn_getCode = (Button) findViewById(R.id.sign_up_btn_get_code);
        btn_signUp = (Button) findViewById(R.id.sign_up_btn_sign_up);

        signUpPresenter = new SignUpPresenterImpl(this);
    }

    @Override
    public void initListener() {
        btn_getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = edt_phoneNum.getText().toString();
                if (phoneNum.length() == 11){
                    signUpPresenter.requestSMSCode(phoneNum);
                }else{
                    Toast.makeText(SignUpActivity.this,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = edt_userName.getText().toString();
                String phoneNum = edt_phoneNum.getText().toString();
                String password = edt_password.getText().toString();
                String smsCode = edt_vfCode.getText().toString();

                if (!password.equals("") && !userName.equals("")){
                    if (!smsCode.equals("")){
                        showProgress("正在注册");
                        signUpPresenter.register(userName,phoneNum,password,smsCode);
                    }else{
                        Toast.makeText(SignUpActivity.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignUpActivity.this,"密码不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onRegisterSuccess() {
        showToast("注册成功，前往主界面");
        hideProgress();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRegisterFail(String msg) {
        showToast("注册失败，"+msg);
    }

    @Override
    public void onRequestSMSCodeSuccess() {
        showToast("验证短信发送成功");
    }

    @Override
    public void onRequestSMSCodeFail(String msg) {
        showToast("验证短信发送失败:"+msg);
    }
}
