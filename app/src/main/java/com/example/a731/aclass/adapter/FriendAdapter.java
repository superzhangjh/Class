package com.example.a731.aclass.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.activity.ChatActivity;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/10/9.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>{

    private List<String> resource;
    private Context context;

    public FriendAdapter(List<String> resource, Context context){
        this.resource = resource;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_friend_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (resource==null)
            return;
        String name = resource.get(position);
        holder.username.setText(name);
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("username",resource.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return resource==null?0:resource.size();
    }

    public void onDataChanged(List<String> resource) {
        this.resource = resource;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        ImageView headImg;
        LinearLayout content;
        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.fragment_friend_tv_item);
            headImg = (ImageView) itemView.findViewById(R.id.fragment_friend_iv_headImg);
            content = (LinearLayout) itemView.findViewById(R.id.fragment_friend_ll_content);
        }
    }
}
