package com.example.a731.aclass.presenter;

import com.example.a731.aclass.data.Users;

/**
 * Created by 郑选辉 on 2017/10/18.
 */

public interface GroupInfoPresenter {
    void getGroupMember(String groupId);

    void getGroup(String groupId);

    void getGroupAdmin(String objectId);


    void removeAdmin(Users users, String groupObjectId);
}
