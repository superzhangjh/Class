package com.example.a731.aclass.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.activity.ChatActivity;
import com.example.a731.aclass.data.Users;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/10/9.
 */

public class FriendAdapter1 extends ArrayAdapter<Users>{

    //private List<Users> usersList;
    private int resourceId;

    public FriendAdapter1(@NonNull Context context, @LayoutRes int resource, List<Users> usersList) {
        super(context, resource,usersList);
        resourceId = resource;
        //this.usersList = usersList;
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

        holder.username.setText(user.getName());
        //holder.headImg.setImageBitmap();
    }
        return view;
    }

    private class ViewHolder{
        TextView username;
        ImageView headImg;
    }

    @Override
    public void setNotifyOnChange(boolean notifyOnChange) {
        super.setNotifyOnChange(notifyOnChange);
    }
}
