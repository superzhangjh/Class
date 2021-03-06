package com.example.a731.aclass.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.a731.aclass.R;
import com.example.a731.aclass.activity.PhotoViewActivity;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class PhotoUploadAdapter extends RecyclerView.Adapter<PhotoUploadAdapter.ViewHolder>{

    private Context context;
    private List<String> srcList;

    public PhotoUploadAdapter(Context context, List<String> srcList) {
        this.context = context;
        this.srcList = srcList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_photo_adapter_upload, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String imageUrl = srcList.get(position);
        Glide.with(context).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL)  .centerCrop().into(holder.img);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PhotoViewActivity.class);
                String photoJson = new Gson().toJson(srcList);
                intent.putExtra("pos",String.valueOf(position));
                intent.putExtra("photoJson",photoJson);
                context.startActivity(intent);
            }
        });
        holder.cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                srcList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return srcList==null?0:srcList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        ImageView cross;
        public ViewHolder(View itemView) {
            super(itemView);
            //信息框
           img = (ImageView) itemView.findViewById(R.id.item_photo_adpter_item);
            cross = (ImageView) itemView.findViewById(R.id.item_photo_adpter_cross);
        }
    }

    public void setOndataChange(List<String> srcList){
        this.srcList = srcList;
        notifyDataSetChanged();
    }

}
