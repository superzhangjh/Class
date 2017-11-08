package com.example.a731.aclass.activity;

import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.FileDownLoadAdapter;
import com.example.a731.aclass.adapter.FileUpLoadAdapter;
import com.example.a731.aclass.adapter.PhotoAdapter;
import com.example.a731.aclass.data.GroupFile;
import com.example.a731.aclass.data.Notice;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.util.BmobUtil;
import com.example.a731.aclass.util.PopItemUtil;
import com.example.a731.aclass.util.ToastUtil;
import com.google.gson.Gson;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.leon.lfilepickerlibrary.utils.Constant;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 郑选辉 on 2017/10/26.
 */

public class DocGatherInfoActivity extends BaseActivity {

    private final int REQUESTCODE_FROM_ACTIVITY = 1000;

    private Toolbar toolbar;
    private CircleImageView ivHead;
    private TextView tvCreatorName;
    private TextView tvTitle;
    private TextView tvDate;
    private TextView tvGroupName;
    private TextView tvDelete;
    private TextView tvContent;
    private RecyclerView recyclePhoto;
    private TextView tvAdjunctInfo;
    private RecyclerView recycleAdjunct;
    private TextView tvUploadInfo;
    private RecyclerView recycleFile;
    private Button btnUpload;

    private List<String> upLoadList = new ArrayList<>();//上传到Bmob后的文件链接
    private FileUpLoadAdapter upLoadAdapter;
    private FileDownLoadAdapter adapter;
    private Notice notice;
    private Context context = DocGatherInfoActivity.this;
    @Override
    protected int getLayoutRes() {
        return R.layout.acticity_doc_gather;
    }

    @Override
    public void initView() {
        findViews();
        initData();
        initToolbal();

        notice = (Notice) getIntent().getSerializableExtra("notice");

        if (notice!=null){
            Glide.with(this).load(notice.getCreator().getHeadImg()).into(ivHead);
            tvCreatorName.setText(notice.getCreator().getName());
            tvGroupName.setText(notice.getGroup().getName());
            tvTitle.setText(notice.getTitle());
            tvDate.setText(notice.getDate());
            tvContent.setText(notice.getContent());
            tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO:删除这条Notice
                    showToast("就不让删除");
                }
            });

            //设置图片
            PhotoAdapter photoAdapter = new PhotoAdapter(this,notice.getPhotoList());
            recyclePhoto.setLayoutManager(new LinearLayoutManager(DocGatherInfoActivity.this,LinearLayoutManager.HORIZONTAL,false));
            recyclePhoto.setAdapter(photoAdapter);

            String selfUsername = BmobUser.getCurrentUser(Users.class).getUsername();


            //附件查看
            if (notice.getFileList().size()>0){
                if (notice.getFileList().get(0).getUrlList().size()>0){
                    tvAdjunctInfo.setVisibility(View.VISIBLE);
                    tvAdjunctInfo.setText("附件列表("+ notice.getFileList().get(0).getUrlList().size()+")");
                }
                GroupFile file = notice.getFileList().get(0);
                List<GroupFile> fileList = new ArrayList<>();
                fileList.add(file);
                adapter = new FileDownLoadAdapter(context,fileList);
                recycleAdjunct.setLayoutManager(new LinearLayoutManager(context));
                recycleAdjunct.setAdapter(adapter);
            }else{
                tvAdjunctInfo.setVisibility(View.GONE);
            }


            //上传及查看功能
            if (notice.getCreator().getUsername().equals(selfUsername)){
                for (GroupFile file:notice.getFileList()){
                    upLoadList = file.getUrlList();
                }
            }   else{
                for (GroupFile file:notice.getFileList()){
                    if (file.getUsers().getUsername().equals(selfUsername)){
                        upLoadList = file.getUrlList();
                    }
                }
            }
            if (upLoadList.size()>0){
                tvUploadInfo.setVisibility(View.VISIBLE);
                tvUploadInfo.setText("已上传"+upLoadList.size()+"个文件");
                btnUpload.setText("继续上传");
            }else{
                tvUploadInfo.setVisibility(View.GONE);
            }

            //上传文件
            upLoadAdapter = new FileUpLoadAdapter(context,upLoadList);
            recycleFile.setLayoutManager(new LinearLayoutManager(context));
            recycleFile.setAdapter(upLoadAdapter);
            upLoadAdapter.setOnItemClickListener(new FileUpLoadAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    upLoadList.remove(position);
                    upLoadAdapter.setOnDataChange(upLoadList);
                    //删除网上数据
                    showToast("未实现");
                }
            });

            /*List<GroupFile> adjunctFileList = new ArrayList<>();//附件列表
            for (GroupFile gFile:notice.getFileList()){
                if (gFile.getUsers().getUsername().equals(notice.getCreator().getUsername())){
                    adjunctFileList.add(gFile);
                }else{
                    newUpLoadFileList.add(gFile);
                }
            }
            //附件列表信息
            if (adjunctFileList.size()>0){
                tvAdjunctInfo.setVisibility(View.VISIBLE);
                tvAdjunctInfo.setText("附件列表("+ adjunctFileList.size()+")");
            }else {
                tvAdjunctInfo.setVisibility(View.GONE);
            }
            //附件列表显示
            FileDownLoadAdapter adjunctAdapter = new FileDownLoadAdapter(context,adjunctFileList);
            recycleAdjunct.setLayoutManager(new LinearLayoutManager(context));
            recycleAdjunct.setAdapter(adjunctAdapter);*/




        }
    }

    private void initToolbal() {
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

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.activity_notice_toolbar);
        ivHead = (CircleImageView) findViewById(R.id.activity_notice_iv_head);
        tvCreatorName = (TextView) findViewById(R.id.activity_notice_tv_creator_name);
        tvTitle = (TextView) findViewById(R.id.activity_notice_tv_title);
        tvDate = (TextView) findViewById(R.id.activity_notice_tv_date);
        tvGroupName = (TextView) findViewById(R.id.activity_notice_tv_group_name);
        tvDelete = (TextView) findViewById(R.id.activity_notice_tv_delete);
        tvContent = (TextView) findViewById(R.id.activity_notice_tv_content);
        recyclePhoto = (RecyclerView) findViewById(R.id.activity_notice_recycle_photo);
        tvAdjunctInfo = (TextView) findViewById(R.id.activity_notice_tv_adjunct_info);
        recycleAdjunct = (RecyclerView) findViewById(R.id.activity_notice_recycle_adjunct);
        tvUploadInfo = (TextView) findViewById(R.id.activity_notice_tv_upload_info);
        recycleFile = (RecyclerView) findViewById(R.id.activity_notice_recycle_file);
        btnUpload = (Button) findViewById(R.id.activity_notice_btn_upload);
    }

    @Override
    public void initListener() {
        //删除
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:判断是否是本人发的，如果是显示该按钮并实现，如果不是隐藏
            }
        });
        //跳转班圈
        tvGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:跳转到班圈
            }
        });
        //上传文档
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LFilePicker()
                        .withActivity(DocGatherInfoActivity.this)
                        .withRequestCode(REQUESTCODE_FROM_ACTIVITY)
                        .start();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            final List<String> datas = data.getStringArrayListExtra(Constant.RESULT_INFO);
            switch (requestCode) {
                case REQUESTCODE_FROM_ACTIVITY:
                    new PopItemUtil(context,"确定上传,取消")
                            .setOnPopItemClick(new PopItemUtil.PopItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            if (position==0){
                                showProgress("正在上传"+datas.size()+"个文件...");
                                for (String filename : datas) {
                                    final BmobFile bmobFile = new BmobFile(new File(filename));
                                    bmobFile.uploadblock(new UploadFileListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e==null){
                                                String path = bmobFile.getFileUrl();
                                                upLoadList.add(path);
                                                //设置样式
                                                tvUploadInfo.setVisibility(View.VISIBLE);
                                                tvUploadInfo.setText("已上传"+upLoadList.size()+"个文件");
                                                btnUpload.setText("重新上传");
                                                //同步到网络
                                                GroupFile groupFile = new GroupFile();
                                                groupFile.setUsers(BmobUser.getCurrentUser(Users.class));
                                                groupFile.setUrlList(upLoadList);
                                                notice.getFileList().add(groupFile);
                                                BmobUtil.updateNotice(notice, notice.getObjectId(), new UpdateListener() {
                                                    @Override
                                                    public void done(BmobException e) {
                                                        if (e == null){
                                                            //TODO:更新成功
                                                            upLoadAdapter.setOnDataChange(upLoadList);
                                                            showToast("上传"+upLoadList.size()+"个文件成功");
                                                            hideProgress();
                                                        }else{
                                                            showToast("上传失败："+upLoadList.size()+"个文件成功");
                                                            hideProgress();
                                                        }
                                                    }
                                                });
                                            } else {
                                                showToast("上传失败");
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                    break;
            }
        }
    }

    @Override
    public void initData() {
    }
}
