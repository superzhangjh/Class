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
import com.example.a731.aclass.data.Conversation;
import com.hyphenate.chat.EMConversation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class MessAdapter extends RecyclerView.Adapter<MessAdapter.ViewHolder>{

    private Context context;
    private List<Conversation> conversations;

    public MessAdapter(Context context){
        this.context = context;
        conversations = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_mess_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Conversation con = conversations.get(position);
        holder.name.setText(con.getName());
        holder.message.setText(con.getLastMess());
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("username",con.getName());
                if (con.getChatType() == EMConversation.EMConversationType.Chat){
                    intent.setClass(context,ChatActivity.class);
                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversations==null?0:conversations.size();
    }

    public void onDataChanged(List<Conversation> conversations) {
        this.conversations = conversations;
        notifyDataSetChanged();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView head;
        TextView name,message;
        LinearLayout content;
        public ViewHolder(View itemView) {
            super(itemView);
            //信息框（包含消息内容和名字）
            content = (LinearLayout) itemView.findViewById(R.id.item_mess_ll_content);
            //信息来源名字
            name = (TextView) itemView.findViewById(R.id.item_mess_tv_name);
            //消息内容
            message = (TextView) itemView.findViewById(R.id.item_mess_tv_message);
            //消息头像
            head = (ImageView) itemView.findViewById(R.id.item_mess_iv_head);
        }
    }

}
