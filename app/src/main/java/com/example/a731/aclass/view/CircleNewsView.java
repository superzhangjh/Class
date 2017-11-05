package com.example.a731.aclass.view;

import com.example.a731.aclass.data.News;
import com.example.a731.aclass.data.Notice;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/11/5.
 */

public interface CircleNewsView {
    void onGetNewsSuccess(List<News> list);

    void onGetNewsFail(String message);

    void onGetGroupFail(String message);
}
