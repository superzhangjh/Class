package com.example.a731.aclass.view;

import com.example.a731.aclass.data.Group;

/**
 * Created by 郑选辉 on 2017/10/19.
 */

public interface JoinGroupView {
    void onSearchGroupSuccess(Group group);

    void onSearchGroupFail(String message);

    void onJoinGroupSuccess();

    void onJoinGroupFail(String message);
}
