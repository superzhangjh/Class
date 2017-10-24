package com.example.a731.aclass.activity;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.EditVoteItemAdapter;
import com.example.a731.aclass.adapter.PhotoAdapter;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.data.Vote;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.DateUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.bmob.v3.BmobUser;

import static com.example.a731.aclass.util.EaseMobUtil.TYPE_DOUBLE_SELECT;
import static com.example.a731.aclass.util.EaseMobUtil.TYPE_SINGLE_SELECT;

/**
 * Created by Administrator on 2017/10/22/022.
 */

public class StartVoteActivity extends BaseActivity implements EditVoteItemAdapter.SaveEditListener{

    private static final int LINE_COUNT = 4;
    private Toolbar toolbar;
    private TextView tvPublic;
    private EditText edtTitle;
    private EditText edtContent;
    private RadioGroup rgType;
    private RadioGroup rgSingleSelect;
    private Button btnAdd;
    private RecyclerView mRecyclerViewList;
    private EditText edtExpirationDate;
    private RadioButton rbSingle,rbDouble;
    private RadioButton rbType1,rbType2,rbType3,rbType4;
    private RecyclerView mRecyclerViewPhoto;
    private TextView tvAddPic;

    private EditVoteItemAdapter itemAdapter;
    private PhotoAdapter photoAdapter;
    private List<Vote.Item> itemList;
    private String[] photoList = new String[9];
    private HashMap map = new HashMap();
    private Vote vote = new Vote();

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_start_a_vote;
    }

    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.activity_start_vote_toolbar);
        tvPublic = (TextView) findViewById(R.id.activity_start_vote_tv_public);
        edtTitle = (EditText) findViewById(R.id.activity_start_vote_edt_title);
        edtContent = (EditText) findViewById(R.id.activity_start_vote_edt_content);
        rgType = (RadioGroup) findViewById(R.id.activity_start_vote_rg_type);
        btnAdd = (Button) findViewById(R.id.activity_start_vote_btn_add);
        mRecyclerViewList = (RecyclerView) findViewById(R.id.activity_start_vote_list);
        edtExpirationDate = (EditText) findViewById(R.id.activity_start_vote_edt_expirationDate);
        rbSingle = (RadioButton) findViewById(R.id.activity_start_vote_rb_single);
        rbDouble = (RadioButton) findViewById(R.id.activity_start_vote_rb_double);
        rgSingleSelect = (RadioGroup) findViewById(R.id.activity_start_vote_rg_select_type);
        rbType1 = (RadioButton) findViewById(R.id.activity_start_vote_rb_type_1);
        rbType2= (RadioButton) findViewById(R.id.activity_start_vote_rb_type_2);
        rbType3 = (RadioButton) findViewById(R.id.activity_start_vote_rb_type_3);
        rbType4 = (RadioButton) findViewById(R.id.activity_start_vote_rb_type_4);
        mRecyclerViewPhoto = (RecyclerView) findViewById(R.id.activity_start_vote_rec_photo);
        tvAddPic = (TextView) findViewById(R.id.activity_start_vote_tv_addPic);

        initToolbar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        //初始化项目Adapter
        //先增2个空子项到itemList
        itemList = new ArrayList<>();
        itemList.add(new Vote.Item());
        itemList.add(new Vote.Item());
        itemAdapter = new EditVoteItemAdapter(getApplicationContext(),itemList);
        itemAdapter.setSaveEditListener(this);
        mRecyclerViewList.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        mRecyclerViewList.setAdapter(itemAdapter);

        //初始化照片Adapter
        photoList = vote.getPhotoList();
        photoAdapter = new PhotoAdapter(getApplicationContext(),photoList);
        mRecyclerViewPhoto.setLayoutManager(new GridLayoutManager(getApplicationContext(),LINE_COUNT));
        mRecyclerViewPhoto.setAdapter(photoAdapter);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        //添加新选项
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemListDataFromEditText();
                for (int i=0;i<itemList.size();i++){
                    String content = itemList.get(i).getItemContent();
                    if (content!=null && !content.equals("")){

                    }else{
                        showToast("请先填写空选项");
                        return;
                    }
                }

                itemList.add(new Vote.Item());
                itemAdapter.SetItemListDataChange(itemList);
                //String json = new Gson().toJson(itemList);
                //Log.i("map result----:",json);
            }
        });

        //添加图片
        tvAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] srcList = {"http://img2.woyaogexing.com/2017/10/13/84d541a98b606416!400x400_big.jpg",
                        "http://img2.woyaogexing.com/2017/10/13/9b19d063b5956bab!400x400_big.jpg",
                        "http://img2.woyaogexing.com/2017/10/13/2f3c280cb3e588bd!400x400_big.jpg",
                        "http://img2.woyaogexing.com/2017/10/13/cd411ab9826c2e2d!400x400_big.jpg",
                        "http://img2.woyaogexing.com/2017/10/13/aeb0a4d826941e8c!400x400_big.jpg"};
                photoList = srcList;
                photoAdapter.setOndataChange(photoList);
                if (photoList[0]!=null){
                    mRecyclerViewPhoto.setVisibility(View.VISIBLE);
                }else {
                    mRecyclerViewPhoto.setVisibility(View.INVISIBLE);
                }
            }
        });

        //发布
        tvPublic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtTitle.getText().toString().equals("")){
                    showToast("标题不能为空");
                    edtTitle.requestFocus();
                    return;
                }else if (edtContent.getText().toString().equals("")){
                    showToast("请输入内容");
                    edtContent.requestFocus();
                    return;
                }else if (edtExpirationDate.getText().toString().equals("")){
                    showToast("请设置截止日期");
                    edtExpirationDate.requestFocus();
                    return;
                }

                //投票类型
                int type;
                switch (rgType.getCheckedRadioButtonId()){
                    case R.id.activity_start_vote_rb_type_1:
                        type = 0;break;
                    case R.id.activity_start_vote_rb_type_2:
                        type = 1;break;
                    case R.id.activity_start_vote_rb_type_3:
                        type = 2;break;
                    default:
                        type = 3;break;
                }
                vote.setType(type);

                if (rgSingleSelect.getCheckedRadioButtonId()==R.id.activity_start_vote_rb_single){
                    vote.setSingleSelect(true);
                }else {
                    vote.setSingleSelect(false);
                }


                vote.setCreator(BmobUser.getCurrentUser(Users.class));//设置用户
                vote.setTitle(edtTitle.getText().toString().trim()); //设置标题
                vote.setContent(edtContent.getText().toString());//设置内容
                vote.setExpirationDate(edtExpirationDate.getText().toString().trim());//设置截止日期
                vote.setDate(DateUtil.MMdd_hhss());//设置创建日期

                setItemListDataFromEditText();//获取EditText的内容
                List<Vote.Item> itemList = itemAdapter.getItemList();
                for (int i=0;i<itemList.size();i++){
                    String content = itemList.get(i).getItemContent();
                    if (content!=null && !content.equals("")){

                    }else{
                        showToast("选项中还有未填写的内容");
                        return;
                    }
                }
                vote.setVoteItems(itemList);//设置投票子选项
                if (photoList!=null){//设置图片
                    vote.setPhotoList(photoList);
                }
                //将Vote转成Json，返回数据给上级
                String voteJson = new Gson().toJson(vote);
                //TODO:将voteJson上传到网络，得到返回结果，如果成功则执行下列代码，失败则return并Toast结果
                Intent intent = new Intent();
                intent.putExtra("voteJson",voteJson);
                Log.i("voteJson",voteJson);
                setResult(RESULT_OK,intent);
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
            //showToast("map内容为空");
        }
    }
}
