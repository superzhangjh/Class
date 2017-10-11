package com.example.a731.aclass.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Gallery;

import java.util.List;

/**
 * Created by Administrator on 2017/10/6/006.
 */

public class CircleGalleryFragment extends BaseFragment {
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Gallery> galleryList;
    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_circle_gallery;
    }

    @Override
    public void initView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.circlegallery__recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.circlegallery_swiperefresh);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        
    }
}
