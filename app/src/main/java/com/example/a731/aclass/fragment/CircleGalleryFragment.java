package com.example.a731.aclass.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.GalleryAdapter;
import com.example.a731.aclass.data.Gallery;

import org.w3c.dom.Text;

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
    private TextView tvLocalPhoto;
    private TextView tvTakePhoto;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_circle_gallery;
    }

    @Override
    public void initView() {
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.circle_gallery_recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.circle_gallery_swiperefresh);
        tvLocalPhoto = (TextView) mRootView.findViewById(R.id.circle_gallery_tv_localPhoto);
        tvTakePhoto = (TextView) mRootView.findViewById(R.id.circle_gallery_tv_takePhoto);


        initRecyclerView();
        
        initUploadPhoto();
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

        GalleryAdapter adapter = new GalleryAdapter(getContext(),galleryList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
        });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setItemViewCacheSize(10);
        adapter.setOnItemClickListener(new GalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                showToast(position+"");
            }
        });
    }

    private void testData() {
        galleryList = new ArrayList<>();

        Gallery gallery = new Gallery();
        gallery.setCreatorName("啊哈");
        gallery.setDate(String.valueOf(new Date()));
        gallery.setLike(11);
        gallery.setIntro("Glide显示的图片");
        List<String> srcList = new ArrayList<>();
        srcList.add("http://img2.woyaogexing.com/2017/10/13/84d541a98b606416!400x400_big.jpg");
        srcList.add("http://img2.woyaogexing.com/2017/10/13/9b19d063b5956bab!400x400_big.jpg");
        srcList.add("http://img2.woyaogexing.com/2017/10/13/2f3c280cb3e588bd!400x400_big.jpg");
        gallery.setSrcList(srcList);
        galleryList.add(gallery);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        
    }
}
