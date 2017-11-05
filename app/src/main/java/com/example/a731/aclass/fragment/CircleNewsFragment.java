package com.example.a731.aclass.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.activity.ReleasingNewsActivity;
import com.example.a731.aclass.adapter.NewsAdapter;
import com.example.a731.aclass.data.News;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.util.DateUtil;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/10/6/006.
 */

public class CircleNewsFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<News> newsList;
    private TextView tvLocalPhoto;
    private TextView tvTakePhoto;
    private TextView tvReleasingNews;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_circle_news;
    }

    @Override
    public void initView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.circle_news_recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.circle_news_swiperefresh);
        tvLocalPhoto = (TextView) mRootView.findViewById(R.id.circle_news_tv_localPhoto);
        tvTakePhoto = (TextView) mRootView.findViewById(R.id.circle_news_tv_takePhoto);
        tvReleasingNews = (TextView) mRootView.findViewById(R.id.circle_news_tv_release_news);
        initReleasingNew();
        initRecyclerView();
        initUploadPhoto();
    }

    //发布动态
    private void initReleasingNew() {
        tvReleasingNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ReleasingNewsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initUploadPhoto() {
        //TODO：上传本地图片
        tvLocalPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //TODO:拍照上传
        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initRecyclerView() {
        testData();

        NewsAdapter adapter = new NewsAdapter(getContext(), newsList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemViewCacheSize(10);
        adapter.setOnItemClickListener(new NewsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showToast(position+"");
            }
        });
    }

    private void testData() {
        newsList = new ArrayList<>();

        News news = new News();
        news.setUsers(BmobUser.getCurrentUser(Users.class));
        news.setDate(DateUtil.yyyyMMdd_hhmmss());
        news.setLike(11);
        news.setContent("Glide显示的图片");
        List<String> srcList = new ArrayList<>();
        srcList.add("http://img2.woyaogexing.com/2017/10/13/84d541a98b606416!400x400_big.jpg");
        srcList.add("http://img2.woyaogexing.com/2017/10/13/9b19d063b5956bab!400x400_big.jpg");
        srcList.add("http://img2.woyaogexing.com/2017/10/13/2f3c280cb3e588bd!400x400_big.jpg");
        news.setPhotoList(srcList);
        newsList.add(news);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        
    }
}
