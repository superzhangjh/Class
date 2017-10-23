package com.example.a731.aclass.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Mess;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.util.EaseMobUtil;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{

    private Context context;
    private List<Mess> messes;
    private String myId;

    public ChatAdapter(Context context, List<Mess> messes){
        this.context = context;
        this.messes = messes;
        myId = BmobUser.getCurrentUser(Users.class).getUsername();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = getViewHolderByViewType(viewType);
        return holder;
    }

    private ViewHolder getViewHolderByViewType(int viewType) {
        View view_get = View.inflate(context, R.layout.activity_chat_item_01, null);
        View view_send = View.inflate(context, R.layout.activity_chat_item_02, null);
        ViewHolder holder = null;
        switch (viewType) {
            case EaseMobUtil.TYPE_GET_MESSAGE:
                holder = new ViewHolder(view_get);
                break;
            case EaseMobUtil.TYPE_SEND_MESSAGE:
                holder = new ViewHolder(view_send);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Mess mess = messes.get(position);
        holder.content.setText(mess.getMessage());
        holder.date.setText(mess.getDate());
    }

    @Override
    public int getItemCount() {
        return messes==null?0:messes.size();
    }

    public void add(Mess msg){
        messes.add(msg);
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView content;
        TextView date;
        public ViewHolder(View itemView) {
            super(itemView);
            //信息框
            content = (TextView) itemView.findViewById(R.id.item_chat_iv_content);
            date = (TextView) itemView.findViewById(R.id.item_chat_iv_date);
        }
    }

    @Override
    public int getItemViewType(int position) {
        int item_view_type;
        String origin = messes.get(position).getCreatorID();
        if (origin.equals(myId)){
            item_view_type = EaseMobUtil.TYPE_SEND_MESSAGE;
        }else {
            item_view_type = EaseMobUtil.TYPE_GET_MESSAGE;
        }
        return item_view_type;
    }
}
