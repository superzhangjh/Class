package com.example.a731.aclass.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.presenter.SignatureQRCodePresenter;
import com.example.a731.aclass.presenter.impl.SignatureQRCodePresenterImpl;
import com.example.a731.aclass.util.QRCodeUtil;
import com.example.a731.aclass.util.ThreadUtils;
import com.example.a731.aclass.view.SignatureQRCodeView;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 郑选辉 on 2017/11/6.
 */

public class SignatureQRCodeActivity extends BaseActivity implements SignatureQRCodeView{

    private EditText edtClassName;
    private Button btnStart,btnEnd;
    private ImageView QRCode;
    //private boolean isEnd = false;
    private String presentGroupId;
    private Bitmap logo;
    private SignatureQRCodePresenter presenter = new SignatureQRCodePresenterImpl(this);
    @Override
    protected int getLayoutRes() {
        presentGroupId = getIntent().getStringExtra("groupId");
        return R.layout.activity_signature_qrcode;
    }

    @Override
    public void initView() {
        edtClassName = (EditText) findViewById(R.id.signature_edt_className);
        btnStart = (Button) findViewById(R.id.signature_btn_start);
        btnEnd = (Button) findViewById(R.id.signature_btn_end);
        QRCode = (ImageView) findViewById(R.id.signature_iv_QRCode);
    }

    @Override
    public void initListener() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignature();
            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //isEnd = true;
                finish();
            }
        });
    }

    private void startSignature() {
        logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        String classname = edtClassName.getText().toString();
        if (classname.equals("")) return;
        presenter.createSignature(classname,presentGroupId);

    }


    @Override
    public void initData() {
    }

    @Override
    public void onCreateSignatureSuccess(final String objectId) {
        btnStart.setVisibility(View.INVISIBLE);
        edtClassName.setVisibility(View.INVISIBLE);
        btnEnd.setVisibility(View.VISIBLE);
        QRCode.setVisibility(View.VISIBLE);
       /* while (!isEnd){
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run(){*/
                    ThreadUtils.runOnBackgroundThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("SignatureQRCodePImpl","开始签到");
                            SimpleDateFormat format = new SimpleDateFormat("HHmmss");
                            String time = format.format(new Date());
                            Log.e("SignatureQRCodePImpl","开始签到2");
                            final Bitmap signQRCode = QRCodeUtil.createImage(2,objectId,logo);
                            ThreadUtils.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    QRCode.setImageBitmap(signQRCode);
                                }
                            });
                       }
                    });
    /*             }
            },10000);
        }*/
    }

    @Override
    public void onCreateSignatureFail(String message) {
        showToast("创建签到失败:"+message);
    }
}
