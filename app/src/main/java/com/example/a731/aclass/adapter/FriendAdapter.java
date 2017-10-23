package com.example.a731.aclass.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Users;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/10/9.
 */

public class FriendAdapter extends ArrayAdapter<Users>{


    //布局文件ID
    private int resourceId;

    public FriendAdapter(@NonNull Context context, int resourceId,List<Users> usersList) {
        super(context,resourceId,usersList);
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Users user = getItem(position);
        View view;
        ViewHolder holder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            holder = new ViewHolder();
            holder.username = (TextView) view.findViewById(R.id.fragment_friend_tv_name);
            holder.headImg = (ImageView) view.findViewById(R.id.fragment_friend_iv_headImg);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.username.setText(user.getUsername());
        if (user.getHeadImg()!=null){
            Glide.with(getContext()).load(user.getHeadImg()).into(holder.headImg);
        }
        return view;
    }

    public void onDataChanged(List<Users> usersList) {
        clear();
        addAll(usersList);
    }

    private class ViewHolder{
        TextView username;
        ImageView headImg;
    }

}

