package com.example.a731.aclass.presenter.impl;

import com.example.a731.aclass.presenter.CreateGroupPresenter;
import com.example.a731.aclass.view.CreateGroupView;

/**
 * Created by 郑选辉 on 2017/9/27.
 */

public class CreateGroupPresenterImpl implements CreateGroupPresenter{

    private CreateGroupView mCreateGroupView;

    public CreateGroupPresenterImpl (CreateGroupView createGroupView){
        mCreateGroupView = createGroupView;
    }
}
