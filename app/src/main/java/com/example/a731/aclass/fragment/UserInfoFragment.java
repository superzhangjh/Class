package com.example.a731.aclass.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.activity.QRCodeActivity;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.UserInfoPresenter;
import com.example.a731.aclass.presenter.impl.UserInfoPresenterImpl;
import com.example.a731.aclass.util.ToastUtil;
import com.example.a731.aclass.view.UserInfoView;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2017/11/7/007.
 */

@SuppressLint("ValidFragment")
public class UserInfoFragment extends BaseFragment  implements UserInfoView {
    private TextView tvSex;//性别图标
    private ImageView itQRCode;//二维码的点击事件
    private TextView tvName;//名字
    private TextView tvUsername;//用户名
    private TextView tvPhoneNember;//;电话号码
    private TextView tvProject;//专业
    private TextView tvHomeland;//家乡
    private TextView tvIntro;//简介

    private Users user = new Users();//用户数据从网络获取，便于修改资料的更新
    private UserInfoPresenter userInfoPresenter = new UserInfoPresenterImpl(this);

    @SuppressLint("ValidFragment")
    public UserInfoFragment(String username){
        userInfoPresenter.getUser(username);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_userinfo;
    }

    @Override
    public void initView() {
        tvSex = (TextView) mRootView.findViewById(R.id.userinfo_tv_sex);
        itQRCode = (ImageView) mRootView.findViewById(R.id.userinfo_iv_qr_droid);
        tvName = (TextView) mRootView.findViewById(R.id.userinfo_tv_name);
        tvUsername = (TextView) mRootView.findViewById(R.id.userinfo_tv_username);
        tvPhoneNember = (TextView) mRootView.findViewById(R.id.userinfo_tv_phonenumber);
        tvProject = (TextView) mRootView.findViewById(R.id.userinfo_tv_project);
        tvHomeland = (TextView) mRootView.findViewById(R.id.userinfo_tv_homeland);
        tvIntro = (TextView) mRootView.findViewById(R.id.userinfo_tv_intro);
    }

    private void initBinderData() {
        ToastUtil.showToast(getContext(),"FragmentUserinfo BinderData");
        if (user!=null){
            //读取User的属性
            String name = user.getName();
            String username = user.getUsername();
            String phoneNumber = user.getMobilePhoneNumber();
            String project =  user.getProject();
            String hoemland = user.getHomeLand();
            String intro = user.getIntro();
            //性别图标显示
            int sex = user.getSex();
            String sexText;
            if (sex==1){
                sexText = "女";
            } else{
                sexText = "男";
            }
            tvName.setText(name);
            tvSex.setText(sexText);
            tvUsername.setText(username);
            tvPhoneNember.setText(phoneNumber);
            //tvIntro.setText(Id);
            tvProject.setText(project);
            tvHomeland.setText(hoemland);
            tvIntro.setText(intro);
        }else {
            showToast("user==null");
        }
    }



    @Override
    protected void initSpringView() {

    }

    @Override
    public void initListener() {
        itQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),QRCodeActivity.class);
                intent.putExtra("headImage",user.getHeadImg());
                intent.putExtra("qrCode",user.getQRCode());
                intent.putExtra("name",user.getName());
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onGetUserSuccess(Users users) {
        user = users;
        initBinderData();
    }

    @Override
    public void onGetUserFail(String message) {

    }
}
