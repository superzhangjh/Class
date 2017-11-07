package com.example.a731.aclass.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Signature;

import java.util.List;

/**
 * Created by 郑选辉 on 2017/11/7.
 */

public class SignatureAdapter extends RecyclerView.Adapter<SignatureAdapter.ViewHolder>{

    private Context context;
    private List<Signature> signatures;
    private OnItemClickListener listener;

    public SignatureAdapter(Context context,List<Signature> signatures){
        this.context = context;
        this.signatures = signatures;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_signature_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Signature signature = signatures.get(position);
        holder.tvClassName.setText(signature.getClassName());
        holder.time.setText(signature.getCreatedAt());

        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position);
            }
        });
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return signatures == null?0:signatures.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvClassName;
        TextView time;
        CardView content;
        public ViewHolder(View itemView) {
            super(itemView);
            tvClassName = (TextView) itemView.findViewById(R.id.signature_item_tv_className);
            time = (TextView) itemView.findViewById(R.id.signature_item_tv_time);
            content = (CardView) itemView.findViewById(R.id.signature_item_cardview_content);
        }
    }

    public void setListData(List<Signature> signatures){
        this.signatures = signatures;
        notifyDataSetChanged();
    }
}
