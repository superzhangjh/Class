package com.example.a731.aclass.presenter;

import com.example.a731.aclass.data.Notice;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/10/25.
 */

public interface ReleasingNoticesPresenter {
    void saveNotice(String presentGroupId, Notice notice, List<String> mImgs);
}
