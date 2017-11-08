package com.example.a731.aclass.activity;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.PhotoAdapter;
import com.example.a731.aclass.adapter.ShowVoteItemAdapter;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.data.Vote;
import com.example.a731.aclass.data.VoteContent;
import com.example.a731.aclass.presenter.VoteInfoPresenter;
import com.example.a731.aclass.presenter.impl.VoteInfoPresenterImpl;
import com.example.a731.aclass.view.VoteInfoView;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/10/21/021.
 */

public class VoteInfoActivity extends BaseActivity implements VoteInfoView{

    private static final int LINE_COUNT = 4;
    private CircleImageView imgHead;
    private Toolbar toolbar;
    private TextView tvType;
    private TextView tvName;
    private TextView tvDate;
    private TextView tvTitle;
    private TextView tvContent;
    private RecyclerView mRecyclerPhoto;
    private RecyclerView mRecyclerItem;
    private TextView tvExpirationDate;
    private TextView tvSelect;
    private CardView tvSelectBg;
    private List<VoteContent.Item> itemList= new ArrayList<>();

    private VoteInfoPresenter presenter = new VoteInfoPresenterImpl(this);
    private String objectId;
    private Vote vote;
    private VoteContent voteContent;

    @Override
    protected int getLayoutRes() {
        return R.layout.interact_vote_info;
    }

    @Override
    public void initView() {
        initToolbar();

        //获取数据
        objectId = getIntent().getStringExtra("objectId");
        showToast(objectId);
        presenter.getVoteByObjectId(objectId);

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
        tvSelectBg = (CardView) findViewById(R.id.interact_vote_tv_select_bg);
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.interact_vote_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        // 显示标题栏左上角的返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //点击toolbar后导航栏 左上角的图标后，退出当前界面
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
    }

    @Override
    public void onGetVoteSuccess(Vote vote) {

        this.vote = vote;
        voteContent = new Gson().fromJson(vote.getContent(),VoteContent.class);
        showToast("获取投票信息成功"+voteContent.getContent());
        updateView();
    }

    @Override
    public void onGetVoteFail(String message) {
        showToast("获取投票信息失败:"+message);
    }

    @Override
    public void onUpdateVoteSuccess() {
        hideProgress();
        showToast("保存数据成功");
    }

    @Override
    public void onUpdateVoteFail(String message) {
        hideProgress();
        showToast("保存数据失败"+message);
    }

    public void updateView(){

        Glide.with(this).load(vote.getCreator().getHeadImg()).into(imgHead);
        if (vote.getCreator().getName()==null){
            tvName.setText(vote.getCreator().getUsername());
        }else {
            tvName.setText(vote.getCreator().getName());
        }
        tvDate.setText(voteContent.getDate());
        int type = voteContent.getType();
        switch (type){
            case 0:tvType.setText("调查");
                tvType.setBackgroundColor(0xff00aeae);
                break;
            case 1:tvType.setText("评选");
                tvType.setBackgroundColor(0xffff7575);
                break;
            case 2:tvType.setText("测试");
                tvType.setBackgroundColor(0xff46a3ff);
                break;
            default:tvType.setText("其他");
                tvType.setBackgroundColor(0xffffd306);
                break;
        }
        tvTitle.setText(voteContent.getTitle());
        tvContent.setText(voteContent.getContent());
        tvDate.setText(voteContent.getDate());
        tvExpirationDate.setText("截止日期:"+voteContent.getExpirationDate());
        //TODO:“请投票”，“已投”和“活动结束”无法点击
        SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
        int now = Integer.valueOf(format.format(new Date()));
        int last = Integer.valueOf(voteContent.getExpirationDate());

        if (last>now){
            tvExpirationDate.setText("剩余时间"+(last-now)+"天");
        }else if (last<=now){
            tvExpirationDate.setText("活动结束");
            tvSelect.setVisibility(View.GONE);
        }

        //相片
        PhotoAdapter photoAdapter = new PhotoAdapter(this,vote.getPhotoList());
        mRecyclerPhoto.setLayoutManager(new GridLayoutManager(this,LINE_COUNT));
        mRecyclerPhoto.setAdapter(photoAdapter);


        //投票子项
        itemList = voteContent.getVoteItems();
        final ShowVoteItemAdapter itemAdapter = new ShowVoteItemAdapter(this,itemList,voteContent.getOptionNumber());
        mRecyclerItem.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerItem.setAdapter(itemAdapter);

        boolean isVote = false;
        String usernameId = BmobUser.getCurrentUser(Users.class).getUsername();
        for (VoteContent.Item option :itemList){
            if (option.getItemSelectId()!=null){
                List<String> selectId = option.getItemSelectId();
                if (selectId.contains(usernameId)){
                    isVote = true;
                }
            }else {
                showToast("option.getItemSelectId()==null");
            }
        }
        if (isVote){
            tvSelectBg.setCardBackgroundColor(0xFF00aeae);
            tvSelect.setText("已投");
            tvSelect.setTextColor(Color.WHITE);
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

                    tvSelectBg.setCardBackgroundColor(0xFF00aeae);
                    tvSelect.setText("已投");
                    tvSelect.setTextColor(Color.WHITE);

                    String data = new Gson().toJson(voteContent);
                    vote.setContent(data);
                    showProgress("正在保存数据");
                    presenter.updateVote(vote.getObjectId(),vote);
                }
            });
        }
    }
}
