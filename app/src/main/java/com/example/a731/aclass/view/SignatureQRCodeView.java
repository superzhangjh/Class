package com.example.a731.aclass.view;

/**
 * Created by 郑选辉 on 2017/11/7.
 */

public interface SignatureQRCodeView {
    void onCreateSignatureSuccess(String objectId);

    void onCreateSignatureFail(String message);
}
