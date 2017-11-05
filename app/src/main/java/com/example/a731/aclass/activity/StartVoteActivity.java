package com.example.a731.aclass.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
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
import com.example.a731.aclass.data.VoteContent;
import com.example.a731.aclass.presenter.StartVotePresenter;
import com.example.a731.aclass.presenter.impl.StartVotePresenterImpl;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.DateUtil;
import com.example.a731.aclass.util.ImageLoderUtil;
import com.example.a731.aclass.util.SharedPreferencesUtil;
import com.example.a731.aclass.view.StartVoteView;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.bmob.v3.BmobUser;

import static com.example.a731.aclass.util.EaseMobUtil.TYPE_DOUBLE_SELECT;
import static com.example.a731.aclass.util.EaseMobUtil.TYPE_SINGLE_SELECT;

/**
 * Created by Administrator on 2017/10/22/022.
 */

public class StartVoteActivity extends BaseActivity implements EditVoteItemAdapter.SaveEditListener,StartVoteView{

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
    private List<VoteContent.Item> itemList = new ArrayList<>();
    private List<String> photoList = new ArrayList<>();
    private HashMap map = new HashMap();
    private Vote vote = new Vote();
    private VoteContent voteContent = new VoteContent();
    private String presentGroupId;

    private StartVotePresenter presenter = new StartVotePresenterImpl(this);

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
        itemList.add(new VoteContent.Item());
        itemList.add(new VoteContent.Item());

        //初始化项目Adapter
        itemAdapter = new EditVoteItemAdapter(getApplicationContext(),itemList);
        itemAdapter.setSaveEditListener(this);
        mRecyclerViewList.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        mRecyclerViewList.setAdapter(itemAdapter);

        //初始化照片Adapter
        //photoList = vote.getPhotoList();
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
                            voteContent.setOptionNumber(size);
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
        presentGroupId = SharedPreferencesUtil.lodaDataFromSharedPreferences(BmobUser.getCurrentUser().getUsername(),this);
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
                itemList.add(new VoteContent.Item());
                itemAdapter.SetItemListDataChange(itemList);
                //String json = new Gson().toJson(itemList);
                //Log.i("map result----:",json);
            }
        });

        //添加图片
        btnAddPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(StartVoteActivity.this).create();//创建一个AlertDialog对象
                dialog.show();//一定要先show出来再设置dialog的参数，不然就不会改变dialog的大小了\
                Window window = dialog.getWindow();
                window.setContentView(R.layout.dialog_select_photo);
                window.setGravity(Gravity.CENTER);//设置对话框在界面底部显示
                dialog.setCanceledOnTouchOutside(true);
                TextView tvTakePhoto = (TextView) window.findViewById(R.id.dialog_select_photo_take);
                TextView tvSelectPhoto = (TextView) window.findViewById(R.id.dialog_select_photo_album);
                //拍照
                tvTakePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String state = Environment.getExternalStorageState();
                        if (state.equals(Environment.MEDIA_MOUNTED)) {
                            Intent getImageByCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(getImageByCamera, ImageLoderUtil.CAPTURE_PHOTO_RESULT_CODE);
                        }
                        else {
                            showToast("请确认已经插入SD卡");
                        }
                        dialog.cancel();
                    }
                });
                //选择照片
                tvSelectPhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(StartVoteActivity.this,ImagePickerActivity.class);
                        startActivityForResult(intent,ImageLoderUtil.CAPTURE_PIICTURE_RESULT_CODE);
                        dialog.cancel();
                    }
                });
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
                }else if (voteContent.getOptionNumber()>itemList.size()){
                    showToast("选项不足请补充");
                    int size = itemList.size();
                    for (int i=0;i<voteContent.getOptionNumber()-size;i++){
                        itemList.add(new VoteContent.Item());
                    }
                    itemAdapter.SetItemListDataChange(itemList);
                }else if (voteContent.getOptionNumber()<2){
                    voteContent.setOptionNumber(1);
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
                voteContent.setType(type);
                vote.setCreator(BmobUser.getCurrentUser(Users.class));//设置用户
                voteContent.setTitle(edtTitle.getText().toString().trim()); //设置标题
                voteContent.setContent(edtContent.getText().toString());//设置内容
                voteContent.setExpirationDate(btnExpirationDate.getText().toString().trim());//设置截止日期
                voteContent.setDate(DateUtil.MMdd_hhss());//设置创建日期

                setItemListDataFromEditText();//获取EditText的内容
                List<VoteContent.Item> itemList = itemAdapter.getItemList();
                for (int i=0;i<itemList.size();i++){
                    String content = itemList.get(i).getItemContent();
                    if (content!=null && !content.equals("")){

                    }else{
                        showToast("选项中还有未填写的内容");
                        return;
                    }
                }
                voteContent.setVoteItems(itemList);//设置投票子选项
                if (photoList!=null){//设置图片
                    vote.setPhotoList(photoList);
                }
                //将Vote转成Json，返回数据给上级
                String voteJson = new Gson().toJson(voteContent);
                vote.setContent(voteJson);
                showProgress("正在保存数据");
                presenter.saveVote(presentGroupId,vote,photoList);
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

    //拍照并保存
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null){
            showToast("获取图片失败");
            return;
        }
        ArrayList<String> mImgs = new ArrayList<>();
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ImageLoderUtil.CAPTURE_PIICTURE_RESULT_CODE){
            mImgs=data.getStringArrayListExtra("selectImg");
        }else if(requestCode == ImageLoderUtil.CAPTURE_PHOTO_RESULT_CODE){
            if (resultCode == Activity.RESULT_OK) {
                String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");// 获取相机返回的数据，并转换为Bitmap图片格式
                FileOutputStream b = null;
                File file = new File("/sdcard/myImage/");
                file.mkdirs();// 创建文件夹
                String fileName = "/sdcard/myImage/"+name;
                try {
                    b = new FileOutputStream(fileName);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        try {
                            b.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        b.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                mImgs.add(fileName);
            }
        }
        photoList.addAll(mImgs);
        photoAdapter.setOndataChange(photoList);
        if (photoList.size()>0){
            mRecyclerViewPhoto.setVisibility(View.VISIBLE);
        }else

        {
            mRecyclerViewPhoto.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onSaveVoteFail(String message) {
        hideProgress();
        showToast("保存投票失败:"+message);
    }

    @Override
    public void onSaveVoteSuccess() {
        hideProgress();
        showToast("保存成功");
        finish();
    }
}
