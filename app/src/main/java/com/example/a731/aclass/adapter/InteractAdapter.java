package com.example.a731.aclass.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Vote;
import com.example.a731.aclass.data.VoteContent;
import com.example.a731.aclass.util.DateUtil;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class InteractAdapter extends RecyclerView.Adapter<InteractAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<Vote> votesList;
    private OnItemClickListener onItemClickListener;

    public InteractAdapter(Context context,List<Vote> votesList){
        this.context = context;
        this.votesList = votesList;
    }

    //更新数据表内容
    public void setListDataChange(List<Vote> votesList){
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
        VoteContent voteContent = new Gson().fromJson(vote.getContent(),VoteContent.class);
        //设置type类型
        int type = voteContent.getType();
        switch (type){
            case 0:
                holder.tvType.setText("调查");
                holder.cvCardColor.setCardBackgroundColor(0xff00aeae);
                break;
            case 1:
                holder.tvType.setText("评选");
                holder.cvCardColor.setCardBackgroundColor(0xffff7575);
                break;
            case 2:
                holder.tvType.setText("测试");
                holder.cvCardColor.setCardBackgroundColor(0xff46a3ff);
                break;
            default:
                holder.tvType.setText("其他");
                holder.cvCardColor.setCardBackgroundColor(0xffffd306);
                break;
        }

        holder.tvTitle.setText(voteContent.getTitle());
        holder.tvContent.setText(voteContent.getContent());

        //设置时间
        if (voteContent.getDate()==null &&voteContent.getDate().equals("")){
            voteContent.setDate(DateUtil.MMdd_hhss());
        }
        holder.tvDate.setText(voteContent.getDate());
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
        CardView cvCardColor;

        public ViewHolder(View view) {
            super(view);
            tvType = (TextView) view.findViewById(R.id.item_circle_vote_type);
            tvTitle = (TextView) view.findViewById(R.id.item_circle_vote_title);
            tvContent = (TextView) view.findViewById(R.id.item_circle_vote_content);
            tvDate = (TextView) view.findViewById(R.id.item_circle_vote_date);
            cvCardColor = (CardView) view.findViewById(R.id.item_circle_vote_card_color);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
}
