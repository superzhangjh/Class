package com.example.a731.aclass.view;

/**
 * Created by 郑选辉 on 2017/10/21.
 */

public interface SystematicNotificationView {
    void onAcceptFriendFail(String message);

    void refresh();

    void onAcceptApplyJoinGroupFail(String message);

    void onRefuseApplyFriendFail(String message);

    void onRefuseApplyJoinGroupFail(String message);
}
