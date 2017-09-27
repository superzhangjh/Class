package com.example.a731.aclass.presenter;

/**
 * Created by 郑选辉 on 2017/9/27.
 */

public interface SignUpPresenter {
    void requestSMSCode(String phoneNum);

    void register(String userName, String phoneNum, String password, String smsCode);
}
