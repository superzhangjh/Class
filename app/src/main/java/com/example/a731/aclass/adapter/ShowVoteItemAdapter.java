package com.example.a731.aclass.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Vote;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class ShowVoteItemAdapter extends RecyclerView.Adapter<ShowVoteItemAdapter.ViewHolder>{

    private Context context;
    private List<Vote.Item> itemList;

    public ShowVoteItemAdapter(Context context, List<Vote.Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_vote_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Vote.Item item = itemList.get(position);
        String itemContent = item.getItemContent();
        int itemCount = item.getItemCount();
        int[] itemSelectId = item.getItemSelectId();

        holder.itemContent.setText(itemContent);
        holder.itemCount.setText("票数"+itemCount);
    }

    @Override
    public int getItemCount() {
        return itemList==null?0:itemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemCount;
        TextView itemContent;
        public ViewHolder(View itemView) {
            super(itemView);
            //信息框
            itemCount = (RadioButton) itemView.findViewById(R.id.item_vote_itemCount);
            itemContent = (TextView) itemView.findViewById(R.id.item_vote_content);
        }
    }
}
