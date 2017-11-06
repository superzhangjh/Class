package com.example.a731.aclass.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.AddGroupAdminAdapter;
import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.presenter.GroupInfoPresenter;
import com.example.a731.aclass.presenter.impl.GroupInfoPresenterImpl;
import com.example.a731.aclass.view.GroupInfoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郑选辉 on 2017/11/6.
 */

public class AddGroupAdminActivity extends BaseActivity implements GroupInfoView {

    private GroupInfoPresenter presenter = new GroupInfoPresenterImpl(this);
    private RecyclerView addGroupAdminContent;
    private AddGroupAdminAdapter adapter;
    private List<Users> usersList = new ArrayList<>();
    private boolean[] isAdmin;
    private boolean isGetUsersOver = false;
    private String groupObjectId;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_add_group_admin;
    }

    @Override
    public void initView() {
        initRecyclerView();
    }

    private void initRecyclerView() {
        addGroupAdminContent = (RecyclerView) findViewById(R.id.add_group_admin_recycler_content);
        adapter = new AddGroupAdminAdapter(this,usersList);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        addGroupAdminContent.setLayoutManager(manager);
        addGroupAdminContent.setAdapter(adapter);

        adapter.setOnItemClickListener(new AddGroupAdminAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (isAdmin[position]){
                    isAdmin[position] = false;
                    presenter.removeAdmin(usersList.get(position),groupObjectId);
                }
            }
        });
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        String groupId = intent.getStringExtra("groupId");
        presenter.getGroup(groupId);
    }

    @Override
    public void onGetGroupMemberFail(String message) {

    }

    @Override
    public void onGetGroupMemberSuccess(List<Users> list) {
        if (isGetUsersOver){
            list.removeAll(usersList);
            usersList.addAll(list);
            int size = usersList.size() - list.size();
            isAdmin = new boolean[usersList.size()];
            for (int i=0;i<size;i++){
                isAdmin[i] = true;
            }
            adapter.setDataChanged(usersList,isAdmin);
        }else{
            usersList = list;
            isGetUsersOver = true;
        }

    }

    @Override
    public void onGetGroupSuccess(List<Group> list) {
        presenter.getGroupMember(list.get(0).getObjectId());
        presenter.getGroupAdmin(list.get(0).getObjectId());
        groupObjectId = list.get(0).getObjectId();
    }

    @Override
    public void onGetGroupFail(String s) {

    }

    @Override
    public void onGetGroupAdminSuccess(List<Users> list) {
        if (isGetUsersOver){
            usersList.removeAll(list);
            List<Users> list1 = usersList;
            usersList.clear();
            usersList = list;
            usersList.addAll(list1);
            isAdmin = new boolean[usersList.size()];
            for (int i=0;i<list.size();i++){
                isAdmin[i] = true;
            }
            adapter.setDataChanged(usersList,isAdmin);
        }else{
            usersList = list;
            isGetUsersOver = true;
        }
    }

    @Override
    public void onGetGroupAdminFail(String message) {

    }

    @Override
    public void onAddOrRemoveAdminSuccess() {
        showToast("修改成功");
        adapter.setDataChanged(usersList,isAdmin);
    }

    @Override
    public void onAddOrRemoveAdminFail(String message) {
        showToast("修改失败:"+message);
    }
}
