package com.example.a731.aclass.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;
import com.example.a731.aclass.activity.PhotoViewActivity;
import com.example.a731.aclass.data.Commend;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.util.ToastUtil;
import com.google.gson.Gson;

import java.util.List;

import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/11/6/016.
 */

public class CommendAdapter extends RecyclerView.Adapter<CommendAdapter.ViewHolder>{

    private Context context;
    private List<Commend> commendList;

    public CommendAdapter(Context context, List<Commend> commendList) {
        this.context = context;
        this.commendList = commendList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_commend, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Commend commend = commendList.get(position);
        final Users[] selfUser = {BmobUser.getCurrentUser(Users.class)};
        if (commend!=null){
            Users user = commend.getUsers();
            Glide.with(context).load(user.getHeadImg()).into(holder.cvHead);
            holder.tvName.setText(user.getName());
            holder.tvDate.setText(commend.getDate());
            holder.tvContent.setText(commend.getContent());
            if (commend.getLikeList().contains(selfUser[0].getUsername())){
                holder.ivLikeIcon.setImageResource(R.drawable.icon_like);//已赞时显示为红色
            }
            holder.tvLikeCount.setText(String.valueOf(commend.getLikeList().size()));//显示赞的个数
        }
        holder.cvLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selfUser[0] = BmobUser.getCurrentUser(Users.class);
                if (commend.getLikeList().contains(selfUser[0].getUsername())){//已赞
                    ToastUtil.showToast(context,"你已经赞过了~");
                }else{//未点赞时的样式设置
                    holder.ivLikeIcon.setImageResource(R.drawable.icon_like);
                    commend.getLikeList().add(selfUser[0].getUsername());
                    holder.tvLikeCount.setText(String.valueOf(commend.getLikeList().size()));//显示赞后的个数
                    ToastUtil.showToast(context,"点赞成功");
                    //TODO:将commend上传到网上但是不刷新列表
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return commendList==null?0:commendList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView cvHead;
        TextView tvName;
        TextView tvDate;
        CardView cvLike;
        ImageView ivLikeIcon;
        TextView tvLikeCount;
        TextView tvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            cvHead = (CircleImageView) itemView.findViewById(R.id.commend_head);
            tvName = (TextView) itemView.findViewById(R.id.commend_name);
            tvDate = (TextView) itemView.findViewById(R.id.commend_date);
            cvLike= (CardView) itemView.findViewById(R.id.commend_like);
            ivLikeIcon = (ImageView) itemView.findViewById(R.id.commend_like_icon);
            tvLikeCount = (TextView) itemView.findViewById(R.id.commend_like_count);
            tvContent = (TextView) itemView.findViewById(R.id.commend_content);
        }
    }

    public void setOnDataChanged(List<Commend> commendList){
        this.commendList = commendList;
        notifyDataSetChanged();
        ToastUtil.showToast(context,"commendlist.size()="+commendList.size());
    }

}
