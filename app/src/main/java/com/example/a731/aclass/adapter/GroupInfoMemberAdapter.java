package com.example.a731.aclass.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;
import com.example.a731.aclass.activity.AddGroupAdminActivity;
import com.example.a731.aclass.activity.UserInfoActivity;
import com.example.a731.aclass.data.Users;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class GroupInfoMemberAdapter extends RecyclerView.Adapter<GroupInfoMemberAdapter.ViewHolder>{

    private Context context;

    //测试数据
    private List<Users> list;
    private String ADD_MORE_MEMBER = "1001";
    private String ADD_MORE_ADMIN = "1002";
    private boolean limit;

    private OnItemClickListener listener;

    public GroupInfoMemberAdapter(Context context,List<Users> memberList){
        this.context = context;
        this.list = memberList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_groupinfo_item_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Users users = list.get(position);
        if (users.getHeadImg()!=null)
            Glide.with(context).load(users.getHeadImg()).crossFade().placeholder(R.drawable.default_head_image).into(holder.head);
        if (users.getUsername().equals(ADD_MORE_MEMBER) || users.getUsername().equals(ADD_MORE_ADMIN)){
            Glide.with(context).load(R.drawable.icon_add).into(holder.head);
        }
        holder.head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnItemClick(position);
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public void setLitmit(boolean limit){
        this.limit = limit;
    }

    public void onDataChange(List<Users> memberlist ){
        list = memberlist;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list ==null?0:list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView head;
        public ViewHolder(View itemView) {
            super(itemView);
            head = (CircleImageView) itemView.findViewById(R.id.groupinfo_rec_memberlist_head);
        }
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }
}
