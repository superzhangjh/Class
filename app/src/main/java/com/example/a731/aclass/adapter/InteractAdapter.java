package com.example.a731.aclass.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Vote;
import com.example.a731.aclass.util.DateUtil;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class InteractAdapter extends RecyclerView.Adapter<InteractAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<Vote> votesList;
    private OnItemClickListener onItemClickListener;

    public InteractAdapter(Context context,List<Vote> votesList){
        for (int i=0;i<4;i++){
            Vote vote = new Vote();
            vote.setType(i);
            vote.setTitle("这是第"+i+"个投票");
            vote.setDate("截止日期:10-11 22:22");
            vote.setContent("投票内容内容投票内容内容投票内容内容投票内容内容投票内容内容投票内容内容投票内容内容投票内容内容投票内容内容投票内容内容投票内容内容投票内容内容投票内容内容投票内容内容投票内容内容投票内容内容");
            votesList.add(vote);
        }
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
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Vote vote = votesList.get(position);

        //设置type类型
        int type = vote.getType();
        switch (type){
            case 0:holder.tvType.setText("调查");break;
            case 1:holder.tvType.setText("评选");break;
            case 2:holder.tvType.setText("测试");break;
            default:holder.tvType.setText("其他");break;
        }

        holder.tvTitle.setText(vote.getTitle());
        holder.tvContent.setText(vote.getContent());

        //设置时间
        if (vote.getDate()==null &&vote.getDate().equals("")){
            vote.setDate(DateUtil.MMdd_hhss());
        }
        holder.tvDate.setText(vote.getDate());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return votesList==null?0:votesList.size();
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener!=null){
            onItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    //外部点击事件调用监听的方法
    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvType;
        TextView tvTitle;
        TextView tvContent;
        TextView tvDate;

        public ViewHolder(View view) {
            super(view);
            tvType = (TextView) view.findViewById(R.id.item_circle_vote_type);
            tvTitle = (TextView) view.findViewById(R.id.item_circle_vote_title);
            tvContent = (TextView) view.findViewById(R.id.item_circle_vote_content);
            tvDate = (TextView) view.findViewById(R.id.item_circle_vote_date);
        }
    }

    public static  interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
}
