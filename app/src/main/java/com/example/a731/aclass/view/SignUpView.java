package com.example.a731.aclass.view;

/**
 * Created by 郑选辉 on 2017/9/27.
 */

public interface SignUpView {
    void onRegisterSuccess();

    void onRegisterFail(String msg);

    void onRequestSMSCodeSuccess();

    void onRequestSMSCodeFail(String msg);
}
