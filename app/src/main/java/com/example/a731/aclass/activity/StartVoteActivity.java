package com.example.a731.aclass.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
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
import java.util.Calendar;
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
    private Button btnExpirationDate;
    private RadioButton rbSingle,rbMutiple;
    private RadioButton rbType1,rbType2,rbType3,rbType4;
    private RecyclerView mRecyclerViewPhoto;
    private Button btnAddPic;

    private EditVoteItemAdapter itemAdapter;
    private PhotoAdapter photoAdapter;
    private List<Vote.Item> itemList = new ArrayList<>();
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
        btnExpirationDate = (Button) findViewById(R.id.activity_start_vote_edt_expirationDate);
        rbSingle = (RadioButton) findViewById(R.id.activity_start_vote_rb_single);
        rbMutiple = (RadioButton) findViewById(R.id.activity_start_vote_rb_mutiple);
        rgSingleSelect = (RadioGroup) findViewById(R.id.activity_start_vote_rg_select_type);
        rbType1 = (RadioButton) findViewById(R.id.activity_start_vote_rb_type_1);
        rbType2= (RadioButton) findViewById(R.id.activity_start_vote_rb_type_2);
        rbType3 = (RadioButton) findViewById(R.id.activity_start_vote_rb_type_3);
        rbType4 = (RadioButton) findViewById(R.id.activity_start_vote_rb_type_4);
        mRecyclerViewPhoto = (RecyclerView) findViewById(R.id.activity_start_vote_rec_photo);
        btnAddPic = (Button) findViewById(R.id.activity_start_vote_tv_addPic);

        initToolbar();
        initData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        //先增2个空子项到itemList
        itemList.add(new Vote.Item());
        itemList.add(new Vote.Item());

        //初始化项目Adapter
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
        rbMutiple.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    AlertDialog.Builder builder = new AlertDialog.Builder(buttonView.getContext());
                    builder.setTitle("请选择选项数量");
                    //    指定下拉列表的显示数据
                    final String[] options = {"2","3","4","5","6","7","8","9"};
                    //    设置一个下拉的列表选择项
                    builder.setItems(options, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            int size = which+1;
                            vote.setOptionNumber(size);
                            rbMutiple.setText("共"+size + "个可选项");
                        }
                    });
                    builder.show();
                }
            }
        });
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
        btnAddPic.setOnClickListener(new View.OnClickListener() {
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

        //选择截至日期
        btnExpirationDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int year = Integer.valueOf(DateUtil.yyyy());
                final int month = Integer.valueOf(DateUtil.MM());
                final int day  = Integer.valueOf(DateUtil.dd());

                new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String _month = String.valueOf(i1);
                        String _day = String.valueOf(i2);
                        if (i1<10){
                           _month =  0 + _month;
                        }else if (i2<10){
                            _day = 0 + _day;
                        }

                        if (i>year){
                            btnExpirationDate.setText(i + _month + _day);
                        }else if (i==year && i1>month){
                            btnExpirationDate.setText(i + _month + _day);
                        }else if (i1==month && i2>day){
                            btnExpirationDate.setText(i + _month + _day);
                        }else {
                            showToast("早于当前日期，请重新选择");
                        }
                    }
                },year,month,day).show();

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
                }else if (btnExpirationDate.getText().toString().equals("")){
                    showToast("请设置截止日期");
                    btnExpirationDate.requestFocus();
                    return;
                }else if (vote.getOptionNumber()>itemList.size()){
                    showToast("选项不足请补充");
                    int size = itemList.size();
                    for (int i=0;i<vote.getOptionNumber()-size;i++){
                        itemList.add(new Vote.Item());
                    }
                    itemAdapter.SetItemListDataChange(itemList);
                }else if (vote.getOptionNumber()<2){
                    vote.setOptionNumber(1);
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
                vote.setCreator(BmobUser.getCurrentUser(Users.class));//设置用户
                vote.setTitle(edtTitle.getText().toString().trim()); //设置标题
                vote.setContent(edtContent.getText().toString());//设置内容
                vote.setExpirationDate(btnExpirationDate.getText().toString().trim());//设置截止日期
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

    //获取edit以输入的数据
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
