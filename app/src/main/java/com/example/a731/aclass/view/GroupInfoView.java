package com.example.a731.aclass.view;

import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Users;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/10/18.
 */

public interface GroupInfoView {
    void onGetGroupMemberFail(String message);

    void onGetGroupMemberSuccess(List<Users> list);

    void onGetGroupSuccess(List<Group> list);

    void onGetGroupFail(String s);
}
