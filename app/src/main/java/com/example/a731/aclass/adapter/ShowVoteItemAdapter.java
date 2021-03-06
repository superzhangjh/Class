package com.example.a731.aclass.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.data.VoteContent;


import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class ShowVoteItemAdapter extends RecyclerView.Adapter<ShowVoteItemAdapter.ViewHolder> implements View.OnClickListener{

    private Context context;
    private List<VoteContent.Item> itemList;
    private int optionNumber;
    private OnItemClickListener onItemClickListener;
    private List<String> checkItemList = new ArrayList<>();
    private boolean[] isCheck;
    private Boolean isVote = false;

    public ShowVoteItemAdapter(Context context, List<VoteContent.Item> itemList,int optionNumber) {
        this.context = context;
        this.itemList = itemList;
        this.optionNumber = optionNumber;
    }

    public void SetItemListDataChange(List<VoteContent.Item> itemList){
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_vote_item, null);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.itemView.setTag(position);
        VoteContent.Item item = itemList.get(position);

        //检查是否投票
        checkIsVote();

        //样式
        //Drawable checkboxDrawable = holder.itemContent.getButtonDrawable();
        if (isVote){//已投票时
            holder.itemContent.setButtonDrawable(null);
            holder.itemCountBg.setVisibility(View.VISIBLE);
            if (checkItemList!=null){
                if (itemList.get(position).getItemSelectId()!=null){
                    int size = itemList.get(position).getItemSelectId().size();
                    holder.itemCount.setText(String.valueOf(size));
                    String username = BmobUser.getCurrentUser(Users.class).getUsername();
                    if (itemList.get(position).getItemSelectId().contains(username)){
                        holder.eachItemBg.setCardBackgroundColor(0xFF00aeae);
                        holder.itemContent.setTextColor(Color.WHITE);
                    }
                }else{
                    holder.itemCount.setText("0");
                }
            } else {
                Toast.makeText(context,"checkItemList==null",Toast.LENGTH_SHORT).show();
            }
        }else {//未投票
            //holder.itemContent.setButtonDrawable(checkboxDrawable);
            holder.itemCountBg.setVisibility(View.INVISIBLE);
        }

        holder.itemContent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String pos  = String.valueOf(position);
                if (isChecked){
                    if (optionNumber==1){
                        checkItemList.clear();//清空其他选择
                        //TODO:点击选项时取消其他已选
                        /*for (int i=0;i<isCheck.length;i++){
                            isCheck[i] = false;
                        }
                        isCheck[position] = isChecked;*/
                        //Toast.makeText(context,"isCheck"+position+" "+isCheck[0]+";"+isCheck[1],Toast.LENGTH_SHORT).show();
                    }
                    checkItemList.add(pos);
                    //Toast.makeText(context,"checkItemList.size():"+checkItemList.size(),Toast.LENGTH_SHORT).show();
                }else {
                    checkItemList.remove(pos);
                    //Toast.makeText(context,"checkItemList.size():"+checkItemList.size(),Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }
        });

        /*if (!isCheck[position-1]){
            holder.itemContent.setChecked(false);
        }*/

        String itemContent = item.getItemContent();
        holder.itemContent.setText(itemContent);
    }

    @Override
    public int getItemCount() {
        int size = itemList==null?0:itemList.size();
        return size;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout eachItem;
        TextView itemCount;
        CheckBox itemContent;
        CardView itemCountBg;
        CardView eachItemBg;
        public ViewHolder(View itemView) {
            super(itemView);
            //信息框
            eachItem = (LinearLayout) itemView.findViewById(R.id.item_vote_eachItem);
            itemCount = (TextView) itemView.findViewById(R.id.item_vote_itemCount);
            itemContent = (CheckBox) itemView.findViewById(R.id.item_vote_itemContent);
            itemCountBg = (CardView) itemView.findViewById(R.id.item_vote_itemCount_bg);
            eachItemBg = (CardView) itemView.findViewById(R.id.item_vote_eachIteBg);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    //外部点击事件调用监听的方法
    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    //外部获取已点击的选项
    public List<String> getcheckItemList(){
        return checkItemList;
    }
    //已投票时调用

    public void setIsVoteTrue(){
        isVote = true;
        notifyDataSetChanged();
    }

    //判断是否投票
    private void checkIsVote() {
        String usernameId = BmobUser.getCurrentUser(Users.class).getUsername();
        for (VoteContent.Item option :itemList){
            List<String> selectId = option.getItemSelectId();
            if (selectId!=null){
                for (int i=0;i<selectId.size();i++){
                    if (String.valueOf(selectId.get(i)).equals(usernameId)){
                        isVote = true;
                    }
                }
            }
        }
        Log.i("###checkIsVote in ShowVoteItemAdapter is ",isVote.toString());
    }

    public boolean getIsVote(){
        return  isVote;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener!=null){
            onItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

}
