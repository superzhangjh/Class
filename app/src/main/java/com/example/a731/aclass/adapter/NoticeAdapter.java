package com.example.a731.aclass.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.activity.DocGatherInfoActivity;
import com.example.a731.aclass.activity.NoticeActivity;
import com.example.a731.aclass.data.Notice;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    private Context context;
    private List<Notice> noticeList;
    private List<Boolean> booleanList = new ArrayList<>();;//读看状态列表

    public NoticeAdapter(Context context, List<Notice> noticeList){
        this.context = context;
        this.noticeList = noticeList;
        if (noticeList.size()!=0)
        for (Notice notice:noticeList){
            booleanList.add(false);
        }
        Log.i("------noticeList.size = ",this.noticeList.size()+"");
    }


    //更新数据表内容
    public void setListData(List<Notice> noticeList){
        this.noticeList = noticeList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_circle_notice_item,parent,false);
        //getIsLookNotice();
        if (noticeList.size()>booleanList.size()){
            for (int i = 0;i<(noticeList.size()-booleanList.size());i++){
                booleanList.add(false);
            }
        }
        //Log.i("------onCreateViewHolder---noticeList.size = ",this.noticeList.size()+"booleanList.size()="+booleanList.size());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        int size = getItemCount();
        final Notice notice = noticeList.get(size-position-1);
        holder.tvTitle.setText(notice.getTitle());
        holder.tvContent.setText(notice.getContent());
        holder.tvDate.setText(notice.getCreatedAt());//TODO:根据时间差显示日期
        holder.cvTip.setVisibility(View.GONE);
        if (notice.getPhotoList().size()!=0){
            holder.tvAdjunct.setVisibility(View.VISIBLE);
        }else {
            holder.tvAdjunct.setVisibility(View.GONE);
        }

        if (booleanList.size()!=0){
            if (!booleanList.get(position)){
                holder.cvTip.setVisibility(View.VISIBLE);
            }else {
                holder.cvTip.setVisibility(View.GONE);
            }
        }


        holder.itemClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                booleanList.set(position,true);//点击隐藏小红点
                setIsLookNotice(position);//存查看状态到数据库,将状态存入本地数据

                    switch (notice.getType()){
                        case 0:
                            Intent intent = new Intent(context, NoticeActivity.class);
                            intent.putExtra("notice", notice);
                            context.startActivity(intent);
                            break;
                        case 2:
                            Intent intent1 = new Intent(context, DocGatherInfoActivity.class);
                            intent1.putExtra("notice", notice);
                            context.startActivity(intent1);
                            break;
                    }


            }
        });
    }

    private void setIsLookNotice(int position) {
        String booleanString = null;
        for (boolean b:booleanList){
            booleanString = b + ",";
        }
        String[] strings = booleanString.split(",");
        SharedPreferences mSharedPreferences = context.getSharedPreferences("noticeLookUpList", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("booleanString","booleanString");
        editor.commit();
        Log.i("--------bL : ",booleanString +" length:"+ strings.length);
    }

    public void getIsLookNotice() {
        SharedPreferences  sp = context.getSharedPreferences("noticeLookUpList", Context.MODE_PRIVATE);
        String string = sp.getString("booleanString", "");
        if (!string.equals("")){
            List<Boolean> newList = new ArrayList<>();
            newList = new Gson().fromJson(string, new TypeToken<List<Boolean>>(){}.getType());
            for (int i=0;i<booleanList.size();i++){
                booleanList.set(i,newList.get(i));
            }
        }
    }


    @Override
    public int getItemCount() {
        return noticeList==null?0:noticeList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout itemClick;
        TextView tvTitle;
        TextView tvContent;
        TextView tvDate;
        TextView tvAdjunct;
        private CardView cvTip;

        public ViewHolder(View view) {
            super(view);
            itemClick = (LinearLayout) itemView.findViewById(R.id.item_circle_notice_item);
            tvTitle = (TextView) itemView.findViewById(R.id.item_circle_notice_tvTitle);
            tvContent = (TextView) itemView.findViewById(R.id.item_circle_notice_tvContent);
            tvDate = (TextView) itemView.findViewById(R.id.item_circle_notice_tvDate);
            tvAdjunct = (TextView) itemView.findViewById(R.id.item_circle_notice_tv_adjunct);
            cvTip = (CardView) itemView.findViewById(R.id.item_circle_notice_cv_tip);
        }
    }
}
