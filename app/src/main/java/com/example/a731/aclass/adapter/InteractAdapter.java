package com.example.a731.aclass.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Vote;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class InteractAdapter extends RecyclerView.Adapter<InteractAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<Vote> votesList;

    public InteractAdapter(Context context, List<Vote> votesList){
        this.context = context;
        this.votesList = votesList;
    }

    //更新数据表内容
    public void setListData(List<Vote> votesList){
        this.votesList = votesList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_circle_vote_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return votesList==null?0:votesList.size();
    }

    @Override
    public void onClick(View v) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //type_item
        ImageView imgNotice;
        TextView tvTitle;
        TextView tvContent;
        TextView tvDate;
        ImageView imgImage;
        ImageView imgFile;
        ImageView imgSound;

        public ViewHolder(View view) {
            super(view);
        }
    }
}
