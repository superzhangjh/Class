package com.example.a731.aclass.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;
import com.example.a731.aclass.activity.UserInfoActivity;
import com.example.a731.aclass.data.News;
import com.example.a731.aclass.data.Users;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    private static final int LINE_COUNT = 3;
    private Context context;
    private List<News> newsList = new ArrayList<>();

    public NewsAdapter(Context context, List<News> newsList){
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_circle_news_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final News news = newsList.get(position);
        final Users users = news.getUsers();
        if (users.getHeadImg() != null) {
            Glide.with(context).load(users.getHeadImg()).into(holder.imgHead);
            holder.imgHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserInfoActivity.class);
                    String username = news.getUsers().getUsername();
                    intent.putExtra("username",username);
                    context.startActivity(intent);
                }
            });
        }
        if (users.getName() != null){
            holder.creatorName.setText(users.getName());
        }else{
            holder.creatorName.setText(users.getUsername());
        }

        holder.date.setText(news.getDate());
        holder.intro.setText(news.getContent());


        String username = users.getUsername();
        if (news.getLike().contains(username)){
            holder.likeIcon.setBackgroundResource(R.drawable.icon_like);
        }else {
            holder.likeIcon.setBackgroundResource(R.drawable.icon_like_gray);
        }

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentUser = BmobUser.getCurrentUser(Users.class).getUsername();
                if (news.getLike().contains(currentUser)){
                    Toast.makeText(context,"你已经赞过了",Toast.LENGTH_SHORT).show();
                }else {
                    if (news.getLike().size()==0){
                        news.setLike(new ArrayList<String>());
                    }
                    List<String> likeList = news.getLike();
                    likeList.add(currentUser);
                    holder.likeIcon.setBackgroundResource(R.drawable.icon_like);
                    int addLike = news.getLike().size();
                    holder.likeCount.setText(addLike+"");
                    news.setLike(likeList);
                    //TODO:更新news的数据到网上，不要刷新本地列表


                }
            }
        });
        holder.likeCount.setText(String.valueOf(news.getLike().size()));
        holder.commend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"暂时无法评论哦亲~",Toast.LENGTH_SHORT).show();
            }
        });

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

    public void setListData(List<News> newsList) {
        this.newsList = newsList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView creatorName;//创建者
        TextView date;//日期
        TextView intro;//介绍
        CardView like;//赞
        TextView likeCount;//赞计数
        ImageView likeIcon;
        TextView commend;//评论(新建一个List储存评论)
        ImageView imgHead;//头像(根据creatorId索引头像图片显示内容)
        RecyclerView photoList;//图片显示的列表
        public ViewHolder(View itemView) {
            super(itemView);
            creatorName = (TextView) itemView.findViewById(R.id.item_circle_news_iv_name);
            date = (TextView) itemView.findViewById(R.id.item_circle_news_tv_date);
            intro = (TextView) itemView.findViewById(R.id.item_circle_news_tv_intro);
            like = (CardView) itemView.findViewById(R.id.item_circle_news_cv_like);
            likeCount = (TextView) itemView.findViewById(R.id.item_circle_tv_like_count);
            likeIcon = (ImageView) itemView.findViewById(R.id.item_circle_tv_like_icon);
            commend = (TextView) itemView.findViewById(R.id.item_circle_news_tv_commend);
            photoList = (RecyclerView) itemView.findViewById(R.id.item_circle_news_recyclerview);
            imgHead = (ImageView) itemView.findViewById(R.id.item_circle_news_iv_head);

        }
    }
}
