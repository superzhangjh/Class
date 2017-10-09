package com.example.a731.aclass.view;

/**
 * Created by 郑选辉 on 2017/9/27.
 */

public interface MainView {
    void onLogoutFail(String error);

    void onDisconnected(int errorCode);

    void onLogoutSuccess();
}
