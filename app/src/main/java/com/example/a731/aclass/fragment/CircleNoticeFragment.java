package com.example.a731.aclass.fragment;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.a731.aclass.R;
import com.example.a731.aclass.activity.NoticeActivity;
import com.example.a731.aclass.adapter.NoticeAdapter;
import com.example.a731.aclass.activity.ReleasingNoticesActivity;
import com.example.a731.aclass.data.Notice;
import com.example.a731.aclass.presenter.CircleNoticePresenter;
import com.example.a731.aclass.presenter.impl.CircleNoticePresenterImpl;
import com.example.a731.aclass.util.SharedPreferencesUtil;
import com.example.a731.aclass.view.CircleNoticeView;
import com.liaoinstan.springview.container.AcFunFooter;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.container.MeituanFooter;
import com.liaoinstan.springview.container.MeituanHeader;
import com.liaoinstan.springview.widget.SpringView;

import cn.bmob.v3.BmobUser;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2017/10/6/006.
 */

public class CircleNoticeFragment extends BaseFragment implements CircleNoticeView {
    private static final int REQUEST_RELEASING_NOTICE = 1001;
    private RecyclerView mRecyclerView;
    private SpringView springView;
    private List<Notice> noticeList = new ArrayList<>();
    private NoticeAdapter adapter;
    //private LinearLayout btn1;

    private String presentGroupId;
    private CircleNoticePresenter presenter = new CircleNoticePresenterImpl(this);

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_circle_base;
    }

    @Override
    public void initView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.circle_base_recyclerview);
        /*btn1 = (LinearLayout) mRootView.findViewById(R.id.notice_head_1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (presenter.queryAdmin()){
                Intent intent = new Intent(getContext(), ReleasingNoticesActivity.class);
                startActivityForResult(intent,REQUEST_RELEASING_NOTICE);
                //}else{
                //showToast("你不是管理员,没有该权限");
                //}
            }
        });*/
        adapter = new NoticeAdapter(getContext(),noticeList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemViewCacheSize(20);
        initData();
    }

    @Override
    protected void initSpringView() {
        springView = (SpringView) mRootView.findViewById(R.id.circle_base_spring_view);
        springView.setFooter(new AliFooter(getContext()));
        springView.setHeader(new AliHeader(getContext()));
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        presentGroupId = SharedPreferencesUtil.lodaDataFromSharedPreferences(BmobUser.getCurrentUser().getUsername(),getContext());
                        if (presentGroupId!=null)
                            presenter.getGroupNotice(presentGroupId);
                        springView.onFinishFreshAndLoad();
                    }
                }, 2000);
            }
            @Override
            public void onLoadmore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        springView.onFinishFreshAndLoad();
                    }
                }, 2000);
            }
        });
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        presentGroupId = SharedPreferencesUtil.lodaDataFromSharedPreferences(BmobUser.getCurrentUser().getUsername(),getContext());
        if (presentGroupId!=null)
            presenter.getGroupNotice(presentGroupId);
    }

    @Override
    public void onGetGroupFail(String message) {
        showToast("获取班圈信息失败:"+message);
    }

    @Override
    public void onGetAdminFail(String message) {
        showToast("获取管理员列表失败:"+message);
    }

    @Override
    public void onGetNoticeSuccess(List<Notice> list) {
        noticeList = list;
        if (adapter==null){
            return;
        }
        adapter.setListData(noticeList);
        //Log.i("--------------CircleNotice","username:"+noticeList.get(0).getCreator().getUsername());
        //showToast("获取通知列表成功"+list.size()+"--"+presentGroupId);
    }

    @Override
    public void onGetNoticeFail(String message) {
        showToast("获取通知列表失败:"+message);
    }

    @Override
    public void onResume() {
        super.onResume();

        presentGroupId = SharedPreferencesUtil.lodaDataFromSharedPreferences(BmobUser.getCurrentUser().getUsername(),getContext());
        if (presentGroupId!=null)
            presenter.getGroupNotice(presentGroupId);
    }

}
