package com.example.a731.aclass.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.EditVoteItemAdapter;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.data.Vote;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.DateUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/10/22/022.
 */

public class StartVoteActivity extends BaseActivity implements EditVoteItemAdapter.SaveEditListener{

    private TextView tvPublic;
    private EditText edtTitle;
    private EditText edtContent;
    private RadioGroup rgType;
    private Button btnAdd;
    private RecyclerView mRecyclerViewList;
    private EditText edtExpirationDate;

    private List<Vote.Item> itemList;
    private HashMap map = new HashMap();
    private Vote vote = new Vote();

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_start_a_vote;
    }

    @Override
    public void initView() {
        tvPublic = (TextView) findViewById(R.id.activity_start_vote_tv_public);
        edtTitle = (EditText) findViewById(R.id.activity_start_vote_edt_title);
        edtContent = (EditText) findViewById(R.id.activity_start_vote_edt_content);
        rgType = (RadioGroup) findViewById(R.id.activity_start_vote_rg_type);
        btnAdd = (Button) findViewById(R.id.activity_start_vote_btn_add);
        mRecyclerViewList = (RecyclerView) findViewById(R.id.activity_start_vote_list);
        edtExpirationDate = (EditText) findViewById(R.id.activity_start_vote_edt_expirationDate);

        initData();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        itemList = new ArrayList<>();
        itemList.add(new Vote.Item());
        itemList.add(new Vote.Item());

        final EditVoteItemAdapter adapter = new EditVoteItemAdapter(getApplicationContext(),itemList);
        adapter.setSaveEditListener(this);
        mRecyclerViewList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerViewList.setAdapter(adapter);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemListDataFromEditText();
                //添加新选项
                //Vote.Item item = new Vote.Item();
                //item.setItemContent("");
                itemList.add(new Vote.Item());
                adapter.SetItemListDataChange(itemList);
                String json = new Gson().toJson(itemList);
                Log.i("map result----:",json);
            }
        });

        //发布
        tvPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vote.setCreator(BmobUser.getCurrentUser(Users.class));
                vote.setTitle(edtTitle.getText().toString().trim());
                vote.setContent(edtContent.getText().toString());
                int type = rgType.getCheckedRadioButtonId();
                vote.setType(type);
                vote.setDate(DateUtil.MMdd_hhss());
                vote.setExpirationDate(edtExpirationDate.getText().toString().trim());

                setItemListDataFromEditText();
                List<Vote.Item> itemList = adapter.getItemList();
                String json = new Gson().toJson(itemList);
                Log.i("itemJson",json);
                finish();
            }
        });
    }

    @Override
    public void onSave(int position, String str) {
        map.put(position,str);
    }

    public void setItemListDataFromEditText() {
        if (map.get(0)!=null && !map.get(0).equals("")){
            for (int i=0;i<itemList.size();i++){
                String content = (String) map.get(i);
                itemList.get(i).setItemContent(content);
            }
        }else {
            showToast("map内容为空");
        }
    }
}
