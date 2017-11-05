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
import com.example.a731.aclass.data.News;
import com.example.a731.aclass.data.Users;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> implements  View.OnClickListener{

    private static final int LINE_COUNT = 3;
    private Context context;
    private List<News> newsList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener = null;

    public NewsAdapter(Context context, List<News> newsList){
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_circle_news_item,parent,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        News news = newsList.get(position);
        Users users = news.getUsers();
        Glide.with(context).load(users.getHeadImg()).into(holder.imgHead);
        holder.creatorName.setText(users.getName());
        holder.date.setText(news.getDate());
        holder.intro.setText(news.getContent());
        holder.like.setText(String.valueOf(news.getLike()));
        //holder.commend = 评论

        //设置图片显示格式
        List<String> srcList = news.getPhotoList();
        PhotoAdapter adapter = new PhotoAdapter(context, srcList);
        holder.photoList.setLayoutManager(new GridLayoutManager(context, LINE_COUNT));
        holder.photoList.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return newsList ==null?0: newsList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView creatorName;//创建者
        TextView date;//日期
        TextView intro;//介绍
        TextView like;//赞
        TextView commend;//评论(新建一个List储存评论)
        ImageView imgHead;//头像(根据creatorId索引头像图片显示内容)
        RecyclerView photoList;//图片显示的列表
        public ViewHolder(View itemView) {
            super(itemView);
            creatorName = (TextView) itemView.findViewById(R.id.item_circle_news_iv_name);
            date = (TextView) itemView.findViewById(R.id.item_circle_news_tv_date);
            intro = (TextView) itemView.findViewById(R.id.item_circle_news_tv_intro);
            like = (TextView) itemView.findViewById(R.id.item_circle_news_tv_like);
            commend = (TextView) itemView.findViewById(R.id.item_circle_news_tv_commend);
            photoList = (RecyclerView) itemView.findViewById(R.id.item_circle_news_recyclerview);
            imgHead = (ImageView) itemView.findViewById(R.id.item_circle_news_iv_head);
        }
    }

    public  interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    //外部调用方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
