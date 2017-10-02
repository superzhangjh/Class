package com.example.a731.aclass.presenter;

import com.example.a731.aclass.data.Users;

import java.io.File;

/**
 * Created by 郑选辉 on 2017/9/27.
 */

public interface CreateGroupPresenter {
    void createGroup(String name, Users creator, File headImg);
}
