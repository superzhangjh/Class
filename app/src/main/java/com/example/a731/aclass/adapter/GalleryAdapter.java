package com.example.a731.aclass.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Gallery;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder>{

    private Context context;
    private List<Gallery> galleryList;

    public GalleryAdapter(Context context, List<Gallery> galleryList){
        this.context = context;
        this.galleryList = galleryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chat_item_01,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView date;
        public ViewHolder(View itemView) {
            super(itemView);
            //信息框
            content = (TextView) itemView.findViewById(R.id.item_chat_iv_content);
            date = (TextView) itemView.findViewById(R.id.item_chat_iv_date);
        }
    }


}
