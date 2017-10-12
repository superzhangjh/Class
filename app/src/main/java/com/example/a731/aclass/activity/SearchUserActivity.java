package com.example.a731.aclass.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.SearchUserPresenter;
import com.example.a731.aclass.presenter.impl.SearchUserPrensterImpl;
import com.example.a731.aclass.view.SearchUserView;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/10/9.
 */

public class SearchUserActivity extends BaseActivity implements SearchUserView{

    private EditText edtUserName;
    private Button btnSearch,btnAdd;
    private ImageView ivHeadImg;
    private TextView tvName;
    private LinearLayout content;

    private SearchUserPresenter searchUserPresenter;

    private String username;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_search;
    }

    @Override
    public void initView() {
        edtUserName = (EditText) findViewById(R.id.search_edt_username);
        btnAdd = (Button) findViewById(R.id.search_btn_add);
        btnSearch = (Button) findViewById(R.id.search_btn_search);
        ivHeadImg = (ImageView) findViewById(R.id.search_iv_headImg);
        tvName = (TextView) findViewById(R.id.search_tv_name);
        content = (LinearLayout) findViewById(R.id.search_ll_preview);

        searchUserPresenter = new SearchUserPrensterImpl(this);
    }

    @Override
    public void initListener() {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = edtUserName.getText().toString();
                searchUserPresenter.searchUser(username);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUserPresenter.addFriend(username,null);
            }
        });
    }

    @Override
    public void initData() {

    }

    @Override
    public void onSearchUserSuccess(List<Users> list) {
        Users users = list.get(0);
        String headImgPath = users.getHeadImg();
        String name = users.getName();
        if (headImgPath!=null){
        }
        if (name!=null){
            tvName.setText(name);
        }else{
            tvName.setText(users.getUsername());
        }
        content.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSearchuserFail(String message) {
        showToast(message);
    }

    @Override
    public void onAddFriendSuccess() {
        showToast("添加好友成功");
        finish();
    }

    @Override
    public void onAddFriendFail(String message) {
        showToast("添加好友失败:"+message);
    }
}
