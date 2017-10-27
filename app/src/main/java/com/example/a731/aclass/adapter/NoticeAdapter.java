package com.example.a731.aclass.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Notice;
import com.example.a731.aclass.view.OnItemClickView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private List<Notice> noticeList;

    private OnItemClickView mOnItemClickViewListener = null;

    //外部Item Click事件
    public void setOnItemClickListener(OnItemClickView listener) {
        this.mOnItemClickViewListener = listener;
    }

    public NoticeAdapter(Context context, List<Notice> noticeList){
        this.context = context;
        this.noticeList = noticeList;
    }

    //更新数据表内容
    public void setListData(List<Notice> noticeList){
        this.noticeList = noticeList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_circle_notice_item,parent,false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        int size = getItemCount();
        Notice notice = noticeList.get(size-position-1);
        holder.tvTitle.setText(notice.getTitle());
        holder.tvContent.setText(notice.getContent());
        holder.tvDate.setText(notice.getCreatedAt());

        if (notice.getPhotoList()!=null){
            holder.imgImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return noticeList==null?0:noticeList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickViewListener != null) {
            mOnItemClickViewListener.onItemClick(v,(int)v.getTag());
        }
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
                imgNotice = (ImageView) itemView.findViewById(R.id.item_circle_notice_imgNotice);
                tvTitle = (TextView) itemView.findViewById(R.id.item_circle_notice_tvTitle);
                tvContent = (TextView) itemView.findViewById(R.id.item_circle_notice_tvContent);
                tvDate = (TextView) itemView.findViewById(R.id.item_circle_notice_tvDate);
                imgImage = (ImageView) itemView.findViewById(R.id.item_circle_notice_imgImage);
                imgFile = (ImageView) itemView.findViewById(R.id.item_circle_notice_imgFile);
                imgSound = (ImageView) itemView.findViewById(R.id.item_circle_notice_imgSound);
        }
    }
}
