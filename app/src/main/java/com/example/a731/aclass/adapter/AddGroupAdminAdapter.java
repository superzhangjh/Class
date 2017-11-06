package com.example.a731.aclass.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Users;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 郑选辉 on 2017/11/6.
 */

public class AddGroupAdminAdapter extends RecyclerView.Adapter<AddGroupAdminAdapter.ViewHolder>{

    private Context context;
    private List<Users> usersList;
    private boolean[] isAdmin;
    private OnItemClickListener listener;

    public AddGroupAdminAdapter(Context context,List<Users> usersList){
        this.context = context;
        this.usersList = usersList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_add_group_admin,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Users users = usersList.get(position);
        if (isAdmin[position]){
            holder.btnAddOrCancel.setText("取消管理员");
        }else {
            holder.btnAddOrCancel.setText("设置为管理员");
        }
        Log.i("AddGroupAdminAdapter","开始绑定数据");
        if (users.getHeadImg()!=null)
            Glide.with(context).load(users.getHeadImg()).into(holder.headImg);
        if (users.getName()!=null){
            holder.name.setText(users.getName());
        }else{
            holder.name.setText(users.getUsername());
        }

        holder.btnAddOrCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        Log.i("AddGroupAdminAdapter",usersList.size()+"");
        return usersList.size();
    }

    public void setDataChanged(List<Users> usersList, boolean[] isAdmin){
        this.usersList = usersList;
        this.isAdmin = isAdmin;
        Log.i("AddGroupAdminAdapter",usersList.size()+"---"+isAdmin.length);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        CircleImageView headImg;
        Button btnAddOrCancel;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.item_add_group_admin_tv_name);
            headImg = (CircleImageView) itemView.findViewById(R.id.item_add_group_admin_iv_head);
            btnAddOrCancel = (Button) itemView.findViewById(R.id.item_add_group_admin_btn_add);
        }
    }
}
