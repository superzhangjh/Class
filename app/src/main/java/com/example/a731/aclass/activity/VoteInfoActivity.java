package com.example.a731.aclass.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.PhotoAdapter;
import com.example.a731.aclass.adapter.ShowVoteItemAdapter;
import com.example.a731.aclass.data.Vote;
import com.google.gson.Gson;

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

        //imgHead.setImageBitmap();
        tvName.setText(vote.getCreator().getName());
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
        tvExpirationDate.setText("截止日期"+vote.getExpirationDate());
        //TODO:“请投票”，“已投”和“活动结束”无法点击
        //tvSelect.setText("");

        //相片
        PhotoAdapter photoAdapter = new PhotoAdapter(getApplicationContext(),vote.getPhotoList());
        mRecyclerPhoto.setLayoutManager(new GridLayoutManager(getApplicationContext(),LINE_COUNT));
        mRecyclerPhoto.setAdapter(photoAdapter);

        //投票子项
        ShowVoteItemAdapter itemAdapter = new ShowVoteItemAdapter(getApplicationContext(),vote.getVoteItems());
        mRecyclerItem.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerItem.setAdapter(itemAdapter);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }
}
