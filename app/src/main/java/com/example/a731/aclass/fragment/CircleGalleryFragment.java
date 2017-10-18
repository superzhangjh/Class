package com.example.a731.aclass.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.GalleryAdapter;
import com.example.a731.aclass.data.Gallery;
import java.util.ArrayList;
import java.util.Date;
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
        return R.layout.fragment_circle_base;
    }

    @Override
    public void initView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.circle_base_recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.circle_base_swiperefresh);
        testData();

        GalleryAdapter adapter = new GalleryAdapter(getContext(),galleryList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemViewCacheSize(10);
    }

    private void testData() {
        galleryList = new ArrayList<>();

        Gallery gallery = new Gallery();
        gallery.setCreatorName("啊哈");
        gallery.setDate(String.valueOf(new Date()));
        gallery.setLike(11);
        gallery.setIntro("Glide显示的图片");
        String[] srcList = {"http://img2.woyaogexing.com/2017/10/13/2f3c280cb3e588bd!400x400_big.jpg"};
        String[] srcList1 = {"http://img2.woyaogexing.com/2017/10/13/84d541a98b606416!400x400_big.jpg",
                "http://img2.woyaogexing.com/2017/10/13/9b19d063b5956bab!400x400_big.jpg",
                "http://img2.woyaogexing.com/2017/10/13/2f3c280cb3e588bd!400x400_big.jpg",
                "http://img2.woyaogexing.com/2017/10/13/cd411ab9826c2e2d!400x400_big.jpg",
                "http://img2.woyaogexing.com/2017/10/13/aeb0a4d826941e8c!400x400_big.jpg"};
        gallery.setSrcList(srcList);
        galleryList.add(gallery);

        Gallery gallery1 = new Gallery();
        gallery1.setCreatorName("z啊很难过");
        gallery1.setDate(String.valueOf(new Date()));
        gallery1.setLike(120);
        gallery1.setIntro("Glide显示的图片");
        gallery1.setSrcList(srcList1);
        galleryList.add(gallery1);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        
    }
}
