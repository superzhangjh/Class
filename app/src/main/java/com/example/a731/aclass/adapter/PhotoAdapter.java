package com.example.a731.aclass.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>{

    private Context context;
    private List<String> srcList;

    public PhotoAdapter(Context context, List<String> srcList) {
        this.context = context;
        this.srcList = srcList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_gallerylist_photo, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String imageUrl = srcList.get(position);
        Glide.with(context).load(imageUrl).centerCrop().into(holder.img);
    }

    @Override
    public int getItemCount() {
        return srcList==null?0:srcList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            //信息框
           img = (ImageView) itemView.findViewById(R.id.gallery_photo_item);
        }
    }

    public void setOndataChange(List<String> srcList){
        this.srcList = srcList;
        notifyDataSetChanged();
    }

}
