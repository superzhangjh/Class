package com.example.a731.aclass.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a731.aclass.R;

import java.util.List;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class FileUpLoadAdapter extends RecyclerView.Adapter<FileUpLoadAdapter.ViewHolder>{
    //添加附件时显示的列表

    private Context context;
    private List<String> pathList;

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }
    private OnItemClickListener mOnItemClickListener = null;

    public FileUpLoadAdapter(Context context, List<String> pathList) {
        this.context = context;
        this.pathList = pathList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_file_uplown, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        String completePath = pathList.get(position);
        String[] paths = completePath.split("\'");
        String filename = paths[paths.length-1];
        String name;
        String type;
        if ((filename.split(".")).length<2){
            name = filename;
            type = "0";
        }else{
            name = filename.split(".")[0];
            type = filename.split(".")[1];
        }
        switch (type){
            case "zip":;
            case "rar":
            case "cab":;
            case "ace":;
            case "7-zip":;
            case "tar":;
            case "gzip":;
            case "uue":;
            case "bz2":;
            case "jar":;
            case "iso":;
            case "z":;
                holder.fileType.setText("压缩");
                holder.fileTypeBg.setCardBackgroundColor(0xFFFF2d2d);
                break;
            case "jpeg":
            case "gif":;
            case "png":;
            case "jpg":;
                holder.fileType.setText("图片");
                holder.fileTypeBg.setCardBackgroundColor(0xFF00aeae);
                break;
            case "doc":
            case "docx":;
            case "pdf":;
                holder.fileType.setText("文档");
                holder.fileTypeBg.setCardBackgroundColor(0xFFadadad);
                break;
            case "ppt":
            case "pptx":;
                holder.fileType.setText("演示");
                holder.fileTypeBg.setCardBackgroundColor(0xFF2894ff);
                break;
            case "xlsx":
            case "xlsm":;
                holder.fileType.setText("表格");
                holder.fileTypeBg.setCardBackgroundColor(0xFF01b468);
                break;
            case "wma":
                holder.fileType.setText("语音");
                holder.fileTypeBg.setCardBackgroundColor(0xFF8cea00);
                break;
            case "mp3":
            case "aac":;
            case "wav":;
            case "flac":;
                holder.fileTypeBg.setCardBackgroundColor(0xFFff5809);
                holder.fileType.setText("音乐");
                break;
            case "mp4":
            case "avi":;
            case "rmb":;
            case "rmvb":;
            case "wmv":;
                holder.fileType.setText("视频");
                holder.fileTypeBg.setCardBackgroundColor(0xFFff8000);
                break;
            case "apk":;
                holder.fileType.setText("应用");
                holder.fileTypeBg.setCardBackgroundColor(0xFF6fb7b7);
                break;
            default:
                holder.fileTypeBg.setCardBackgroundColor(0xFFc07ab8);
                holder.fileType.setText("其他");
                break;
        }
        holder.fileName.setText(name);
        holder.filePath.setText(completePath);
        holder.fileTypeCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pathList==null?0:pathList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView fileTypeBg;
        TextView fileType;
        TextView fileName;
        TextView filePath;
        ImageView fileTypeCross;

        public ViewHolder(View itemView) {
            super(itemView);
            fileTypeBg = (CardView) itemView.findViewById(R.id.item_file_type_bg);
            fileType = (TextView) itemView.findViewById(R.id.item_file_type);
            fileName = (TextView) itemView.findViewById(R.id.item_file_name);
            filePath = (TextView) itemView.findViewById(R.id.item_file_type_path);
            fileTypeCross = (ImageView) itemView.findViewById(R.id.item_file_type_cross);
        }
    }

    public void setOnDataChange(List<String> pathList){
        this.pathList = pathList;
        notifyDataSetChanged();
    }

}
