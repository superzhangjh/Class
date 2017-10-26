package com.example.a731.aclass.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Gallery;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder>{

    private static final int LINE_COUNT = 4;
    private Context context;
    private List<Gallery> galleryList;

    public GalleryAdapter(Context context, List<Gallery> galleryList){
        this.context = context;
        this.galleryList = galleryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_circle_gallery_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Gallery gallery = galleryList.get(position);
        //Bitmap headBitmap = getHeadBitmapForCreatorId();
        //holder.imgHead.setImageBitmap();
        holder.creatorName.setText(gallery.getCreatorName());
        holder.date.setText(gallery.getDate());
        holder.intro.setText(gallery.getIntro());
        holder.like.setText(String.valueOf(gallery.getLike()));
        //holder.commend = 评论

        //设置图片显示格式
        List<String> srcList = gallery.getSrcList();
        int srcListCount = srcList.size();
        if(srcListCount==0){
            return;
        }
        if (srcListCount==1){
            holder.imgFirst.setVisibility(View.VISIBLE);
            holder.photoList.setVisibility(View.GONE);
            String imageUrl = srcList.get(0);
            Glide.with(context).load(imageUrl).fitCenter().into(holder.imgFirst);
        } else {
            holder.imgFirst.setVisibility(View.GONE);
            holder.photoList.setVisibility(View.VISIBLE);
            PhotoAdapter adapter = new PhotoAdapter(context,srcList);
            holder.photoList.setLayoutManager(new GridLayoutManager(context,LINE_COUNT));
            holder.photoList.setAdapter(adapter);
        }
    }

    @Override
    public int getItemCount() {
        return galleryList==null?0:galleryList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView creatorName;//创建者
        TextView date;//日期
        TextView intro;//介绍
        TextView like;//赞
        TextView commend;//评论(新建一个List储存评论)
        ImageView imgHead;//头像(根据creatorId索引头像图片显示内容)
        ImageView imgFirst;//第一张图片
        RecyclerView photoList;//图片显示的列表
        public ViewHolder(View itemView) {
            super(itemView);
            creatorName = (TextView) itemView.findViewById(R.id.item_circle_gallery_iv_name);
            date = (TextView) itemView.findViewById(R.id.item_circle_gallery_tv_date);
            intro = (TextView) itemView.findViewById(R.id.item_circle_gallery_tv_intro);
            like = (TextView) itemView.findViewById(R.id.item_circle_gallery_tv_like);
            commend = (TextView) itemView.findViewById(R.id.item_circle_gallery_tv_commend);
            photoList = (RecyclerView) itemView.findViewById(R.id.item_circle_gallery_recyclerview);
            imgHead = (ImageView) itemView.findViewById(R.id.item_circle_gallery_iv_head);
            imgFirst = (ImageView) itemView.findViewById(R.id.item_circle_gallery_img_first);
        }
    }
}
