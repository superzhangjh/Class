package com.example.a731.aclass.view;


import com.example.a731.aclass.data.Notice;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/10/25.
 */

public interface CircleNoticeView {

    void onGetGroupFail(String message);

    void onGetAdminFail(String message);

    void onGetNoticeSuccess(List<Notice> list);

    void onGetNoticeFail(String message);
}
