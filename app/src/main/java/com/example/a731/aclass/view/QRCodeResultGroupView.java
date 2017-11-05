package com.example.a731.aclass.view;

import com.example.a731.aclass.data.Group;

/**
 * Created by 郑选辉 on 2017/11/4.
 */

public interface QRCodeResultGroupView {

    void onGetGroupSuccess(Group group);

    void onGetGroupFail(String message);

    void onJoinGroupSuccess();

    void onJoinGroupFail(String message);
}
