package com.example.a731.aclass.view;

import com.example.a731.aclass.data.Users;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/11/7.
 */

public interface SignatureDetailView {
    void onGetSignatureMemberSuccess(List<Users> groupMembers, List<Users> signMembers);

    void onGetSignatureMemberFail(String message);
}
