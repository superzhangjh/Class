package com.example.a731.aclass.presenter;

import com.example.a731.aclass.data.News;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/11/5.
 */

public interface ReleasingNewsPresenter {
    void saveNews(String presentGroupId,News news, List<String> mImgs);
}
