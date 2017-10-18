package com.example.a731.aclass.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Users;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/10/16/016.
 */

public class ModifiedUserDataActivity extends BaseActivity{

    private TextView tvUsername;
    private EditText edtName;
    private TextView tvPhonenumber;
    private TextView tvId;
    private EditText edtProject;
    private EditText edtLocal;
    private EditText edtIntro;
    private ImageView imgCross;
    private ImageView imgCheck;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_modified_user_data;
    }

    @Override
    public void initView() {
        tvUsername = (TextView) findViewById(R.id.modified_edt_username);
        edtName = (EditText) findViewById(R.id.modified_edt_name);
        tvPhonenumber = (TextView) findViewById(R.id.modified_edt_phonenumber);
        tvId = (TextView) findViewById(R.id.modified_edt_id);
        edtProject = (EditText) findViewById(R.id.modified_edt_project);
        edtLocal = (EditText) findViewById(R.id.modified_edt_local);
        edtIntro = (EditText) findViewById(R.id.modified_edt_intro);
        imgCross = (ImageView) findViewById(R.id.modified_img_cross);
        imgCheck = (ImageView) findViewById(R.id.modified_img_check);


        imgCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = tvUsername.getText().toString().trim();
                String name = edtName.getText().toString().trim();
                String phoneNumber = tvPhonenumber.getText().toString().trim();
                int id = Integer.parseInt(tvId.getText().toString().trim());
                String project = edtProject.getText().toString().trim();
                String location = edtLocal.getText().toString().trim();
                String intro = edtIntro.getText().toString();

                Users user = new Users();

                if (username==null &&username.equals("")){
                    user.setUsername("暂未设置");
                }else{
                    user.setUsername(username);
                }
                if (phoneNumber==null &&phoneNumber.equals("")){
                    user.setMobilePhoneNumber("暂未设置");
                }else{
                    user.setMobilePhoneNumber(phoneNumber);
                }
                user.setId(id);
                user.setName(name);
                user.setProject(project);
                user.setHomeLand(location);
                user.setIntro(intro);

                //TODO:b保存数据到Bmob

                finish();
            }
        });
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        showInfomation();
    }

    //展示数据
    private void showInfomation() {
        Users user = BmobUser.getCurrentUser(Users.class);
        String username = user.getUsername();
        String name = user.getName();
        String phoneNumber = user.getMobilePhoneNumber();
        int id = user.getId();
        String project = user.getProject();
        String location = user.getHomeLand();
        String intro = user.getIntro();

        tvUsername.setText(username);
        edtName.setHint(name);
        tvPhonenumber.setText(phoneNumber);
        if (id!=0){
            tvId.setText(id);
        }
        edtProject.setHint(project);
        edtLocal.setHint(location);
        edtIntro.setHint(intro);
    }
}
