package com.example.a731.aclass.view;

import com.example.a731.aclass.data.Signature;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/11/7.
 */

public interface SignatureView {
    void onGetSignatureSuccess(List<Signature> list);

    void onGetSignatureFail(String message);
}
