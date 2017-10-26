package com.example.a731.aclass.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.ModifiedUserDataPresenter;
import com.example.a731.aclass.presenter.impl.ModifiedUserDataPresenterImpl;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.view.ModifiedUserDataView;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/10/16/016.
 */

public class ModifiedUserDataActivity extends BaseActivity implements ModifiedUserDataView{

    private TextView tvUsername;
    private EditText edtName;
    private TextView tvPhonenumber;
    private EditText edtProject;
    private EditText edtLocal;
    private EditText edtIntro;
    private ImageView imgCross;
    private ImageView imgCheck;

    private String objectId;
    private Users user = BmobUser.getCurrentUser(Users.class);

    private ModifiedUserDataPresenter mPresenter = new ModifiedUserDataPresenterImpl(this);

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_modified_user_data;
    }

    @Override
    public void initView() {
        tvUsername = (TextView) findViewById(R.id.modified_edt_username);
        edtName = (EditText) findViewById(R.id.modified_edt_name);
        tvPhonenumber = (TextView) findViewById(R.id.modified_edt_phonenumber);
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
                showProgress("正在保存");
                String name = edtName.getText().toString().trim();
                String project = edtProject.getText().toString().trim();
                String location = edtLocal.getText().toString().trim();
                String intro = edtIntro.getText().toString();

                Users user = new Users();

                if (!name.equals(""))
                    user.setName(name);
                if (!project.equals(""))
                    user.setProject(project);
                if (!location.equals(""))
                    user.setHomeLand(location);
                if (!intro.equals(""))
                    user.setIntro(intro);

                mPresenter.save(user,objectId);

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

        String username = user.getUsername();
        String name = user.getName();
        String phoneNumber = user.getMobilePhoneNumber();
        String project = user.getProject();
        String location = user.getHomeLand();
        String intro = user.getIntro();
        objectId = user.getObjectId();

        tvUsername.setText(username);
        edtName.setHint(name);
        tvPhonenumber.setText(phoneNumber);
        edtProject.setHint(project);
        edtLocal.setHint(location);
        edtIntro.setHint(intro);
    }

    @Override
    public void onSaveSuccess() {
        hideProgress();
        showToast("保存成功");
        finish();
    }

    @Override
    public void onSaveFail(String message) {
        hideProgress();
        showToast("保存失败:"+message);
    }
}
