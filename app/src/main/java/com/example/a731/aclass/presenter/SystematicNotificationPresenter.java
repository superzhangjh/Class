package com.example.a731.aclass.presenter;

/**
 * Created by 郑选辉 on 2017/10/21.
 */

public interface SystematicNotificationPresenter {
    void acceptApplyFriend(String username);

    void acceptApplyJoinGroup(String username, String groupId);

    void refuseApplyFriend(String username);

    void refuseApplyJoinGroup(String username, String groupId);
}
