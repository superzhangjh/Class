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
import com.example.a731.aclass.activity.MainActivity;
import com.example.a731.aclass.activity.UserInfoActivity;
import com.example.a731.aclass.data.Users;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class GroupInfoMemberAdapter extends RecyclerView.Adapter<GroupInfoMemberAdapter.ViewHolder>{

    private Context context;

    //测试数据
    private List<Users> list;
    private String ADD_MORE_MEMBER = "1001";

    public GroupInfoMemberAdapter(Context context,List<Users> memberList){
        this.context = context;
        this.list = memberList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=null;
        if (view==null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_groupinfo_item_member, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String name = list.get(position).getUsername();
        holder.name.setText(name);
        //holder.head.setBackgroundResource();
        holder.itemClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getUsername()==ADD_MORE_MEMBER){
                    //跳转到新增
                    //Intent intent = new Intent(context, 新增.class);
                    //context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context,UserInfoActivity.class);
                    context.startActivity(intent);
                }
            }
        });
    }

    public void onDataChange(List<Users> memberlist ){
        list = memberlist;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView head;
        TextView name;
        LinearLayout itemClick;
        public ViewHolder(View itemView) {
            super(itemView);
            itemClick = (LinearLayout) itemView.findViewById(R.id.groupinfo_rec_memberlist_click);
            name = (TextView) itemView.findViewById(R.id.groupinfo_rec_memberlist_name);
            head = (ImageView) itemView.findViewById(R.id.groupinfo_rec_memberlist_head);
        }
    }
}
