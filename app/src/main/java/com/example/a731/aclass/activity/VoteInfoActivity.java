package com.example.a731.aclass.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.PhotoAdapter;
import com.example.a731.aclass.adapter.ShowVoteItemAdapter;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.data.Vote;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/10/21/021.
 */

public class VoteInfoActivity extends BaseActivity{

    private static final int LINE_COUNT = 4;
    private CircleImageView imgHead;
    private TextView tvType;
    private TextView tvName;
    private TextView tvDate;
    private TextView tvTitle;
    private TextView tvContent;
    private RecyclerView mRecyclerPhoto;
    private RecyclerView mRecyclerItem;
    private TextView tvExpirationDate;
    private TextView tvSelect;
    private List<Vote.Item> itemList= new ArrayList<>();

    private Vote vote;

    @Override
    protected int getLayoutRes() {
        return R.layout.interact_vote_info;
    }

    @Override
    public void initView() {
        imgHead = (CircleImageView) findViewById(R.id.interact_vote_img_head);
        tvType = (TextView) findViewById(R.id.interact_vote_tv_type);
        tvName = (TextView) findViewById(R.id.interact_vote_tv_name);
        tvDate = (TextView) findViewById(R.id.interact_vote_tv_date);
        tvTitle = (TextView) findViewById(R.id.interact_vote_tv_title);
        tvContent = (TextView) findViewById(R.id.interact_vote_tv_content);
        mRecyclerPhoto = (RecyclerView) findViewById(R.id.interact_vote_recycler_photo);
        mRecyclerItem = (RecyclerView) findViewById(R.id.interact_vote_recycler_item);
        tvExpirationDate = (TextView) findViewById(R.id.interact_vote_tv_expirationDate);
        tvSelect = (TextView) findViewById(R.id.interact_vote_tv_select);

        //获取数据
        String voteJson = getIntent().getStringExtra("vote");
        vote = new Gson().fromJson(voteJson,Vote.class);
        Log.i("VoteJson","VoteJson:" + voteJson);

        ;
        Glide.with(this).load(vote.getCreator().getHeadImg()).into(imgHead);
        if (vote.getCreator().getName()==null){
            tvName.setText(vote.getCreator().getUsername());
        }else {
            tvName.setText(vote.getCreator().getName());
        }
        tvDate.setText(vote.getDate());
        int type = vote.getType();
        switch (type){
            case 0:tvType.setText("调查");break;
            case 1:tvType.setText("评选");break;
            case 2:tvType.setText("测试");break;
            default:tvType.setText("其他");break;
        }
        tvTitle.setText(vote.getTitle());
        tvContent.setText(vote.getContent());
        tvDate.setText(vote.getDate());
        tvExpirationDate.setText("截止日期:"+vote.getExpirationDate());
        //TODO:“请投票”，“已投”和“活动结束”无法点击
        //tvSelect.setText("");

        //相片
        PhotoAdapter photoAdapter = new PhotoAdapter(getApplicationContext(),vote.getPhotoList());
        mRecyclerPhoto.setLayoutManager(new GridLayoutManager(getApplicationContext(),LINE_COUNT));
        mRecyclerPhoto.setAdapter(photoAdapter);


        //投票子项
        itemList = vote.getVoteItems();
        final ShowVoteItemAdapter itemAdapter = new ShowVoteItemAdapter(getApplicationContext(),itemList,vote.getOptionNumber());
        mRecyclerItem.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerItem.setAdapter(itemAdapter);

        boolean isVote = false;
        String usernameId = BmobUser.getCurrentUser(Users.class).getUsername();
        for (Vote.Item option :itemList){
            if (option.getItemSelectId()!=null){
                List<String> selectId = option.getItemSelectId();
                for (int i=0;i<selectId.size();i++){
                    if (String.valueOf(selectId.get(i)).equals(usernameId)){
                        isVote = true;
                    }
                }
            }else {
                showToast("option.getItemSelectId()==null");
            }
        }
        if (isVote){
            tvSelect.setBackgroundColor(Color.YELLOW);
            tvSelect.setText("已投");
            tvSelect.setTextColor(Color.BLACK);
        }else {
            tvSelect.setText("投票");
            tvSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemAdapter.setIsVoteTrue();
                    List<String> checkItemList = itemAdapter.getcheckItemList();
                    String username = BmobUser.getCurrentUser(Users.class).getUsername();
                    for (String str:checkItemList){
                        int pos = Integer.valueOf(str);
                        if (itemList.get(pos).getItemSelectId()==null){
                            itemList.get(pos).setItemSelectId(new ArrayList<String>());
                        }
                        List<String> list = itemList.get(pos).getItemSelectId();
                        list.add(username);
                        itemList.get(pos).setItemSelectId(list);
                        itemAdapter.SetItemListDataChange(itemList);
                    }

                    tvSelect.setBackgroundColor(Color.YELLOW);
                    tvSelect.setText("已投");
                    tvSelect.setTextColor(Color.BLACK);

                    //返回结果给Fragment
                    String position = getIntent().getStringExtra("position");
                    String data = new Gson().toJson(vote);
                    Intent intent = new Intent();
                    intent.putExtra("vote",data);
                    setResult(RESULT_OK,intent);

                    //TODO:更新网上数据
                }
            });

        }


    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }
}
