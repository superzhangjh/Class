package com.example.a731.aclass.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import com.example.a731.aclass.R;
import com.example.a731.aclass.adapter.MessAdapter;
import com.example.a731.aclass.data.Mess;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/15/015.
 */

public class MessFragment extends BaseFragment{

    private RecyclerView recyclerView;
    private List<Mess> messes = new ArrayList<>();

    @Override
    protected int getLayoutRes() {
        messes = new ArrayList<>();
        for(int i=0;i<10;i++){
            Mess mess = new Mess();
            mess.setName("群"+i);
            mess.setMessage("你已被移出群聊！");
            messes.add(mess);
        }
        return R.layout.fragment_mess;
    }

    @Override
    public void initView() {
        initRecyclerview();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }

    private void initRecyclerview() {
        recyclerView = (RecyclerView) mRootView.findViewById(R.id.mess_recycle_view);
        MessAdapter adapter = new MessAdapter(getContext(),messes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
}
