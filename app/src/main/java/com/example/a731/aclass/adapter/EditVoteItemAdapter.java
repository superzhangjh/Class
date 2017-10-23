package com.example.a731.aclass.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextSwitcher;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Vote;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class EditVoteItemAdapter extends RecyclerView.Adapter<EditVoteItemAdapter.ViewHolder>{

    private Context context;
    private List<Vote.Item> itemList;
    private boolean typeSingle;//是否为单选
    private SaveEditListener mSaveEditListener;

    public EditVoteItemAdapter(Context context,List<Vote.Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public interface SaveEditListener{
        void onSave(int position,String str);
    }

    public void setSaveEditListener(SaveEditListener listener){
        this.mSaveEditListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_edit_vote_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.edtItem.setTag(position);

        final Vote.Item item = itemList.get(position);


        if (item.getItemContent()==null){
            holder.edtItem.setHint("选项"+(position+1));
        }else {
            holder.edtItem.setText(item.getItemContent());
        }
        holder.edtItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ((int)(holder.edtItem.getTag()) == position) {//设置tag解决错乱问题
                    mSaveEditListener.onSave(position,s.toString());
                }
            }
        });
    }

    public List<Vote.Item> getItemList(){
        return itemList;
    }

    public void SetItemListDataChange(List<Vote.Item> itemList){
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemList==null?0:itemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        EditText edtItem;
        public ViewHolder(View itemView) {
            super(itemView);
            //信息框
            edtItem = (EditText) itemView.findViewById(R.id.item_edit_vote_item);
        }
    }
}
