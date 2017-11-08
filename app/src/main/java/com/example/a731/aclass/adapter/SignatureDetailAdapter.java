package com.example.a731.aclass.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Users;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 郑选辉 on 2017/11/7.
 */

public class SignatureDetailAdapter extends RecyclerView.Adapter<SignatureDetailAdapter.ViewHolder>{

    private List<Users> groupMembers;
    private List<String> signMembers;
    private Context context;
    public SignatureDetailAdapter(Context context){
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_signature_detail_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Users users = groupMembers.get(position);
        if (!signMembers.contains(users.getUsername())){
            holder.content.setBackgroundColor(context.getResources().getColor(R.color.colorOrange));
        }
        if (users.getHeadImg()!=null)
            Glide.with(context).load(users.getHeadImg()).into(holder.head);
        if (users.getName()!=null){
            holder.name.setText(users.getName());
        }else{
            holder.name.setText(users.getUsername());
        }
    }

    @Override
    public int getItemCount() {
        return groupMembers==null?0:groupMembers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout content;
        CircleImageView head;
        TextView name;
        public ViewHolder(View itemView) {
            super(itemView);
            content = (LinearLayout) itemView.findViewById(R.id.signature_detail_item_ll_content);
            head = (CircleImageView) itemView.findViewById(R.id.signature_detail_item_cv_headImg);
            name = (TextView) itemView.findViewById(R.id.signature_detail_item_tv_name);
        }
    }

    public void setListData(List<Users> groupMembers, List<String> signMembers){
        this.groupMembers = groupMembers;
        this.signMembers = signMembers;
        notifyDataSetChanged();
    }
}
