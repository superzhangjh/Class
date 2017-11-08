package com.example.a731.aclass.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.a731.aclass.R;
import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.GroupFile;
import com.example.a731.aclass.data.Notice;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.util.DownloadImageServiceUtil.DownLoadImageService;
import com.example.a731.aclass.util.DownloadImageServiceUtil.ImageDownLoadCallBack;
import com.example.a731.aclass.util.PopItemUtil;
import com.example.a731.aclass.util.ToastUtil;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2017/9/16/016.
 */

public class FileDownLoadAdapter extends RecyclerView.Adapter<FileDownLoadAdapter.ViewHolder>{

    private static final int MSG_VISIBLE = 3001;
    private static final int MSG_ERROR = 3002;
    private long delayTime = 1000;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what==MSG_VISIBLE){
                ToastUtil.showToast(context,"保存成功");
                return true;
            }else if (msg.what==MSG_ERROR){
                ToastUtil.showToast(context,"保存失败");
                return true;
            }
            return false;
        }
    });

    private Context context;
    private List<GroupFile> groupFileList;

    public FileDownLoadAdapter(Context context, List<GroupFile> groupFiles) {
        this.context = context;
        this.groupFileList  =groupFiles;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_file_downlown, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final GroupFile groupFile = groupFileList.get(position);
        Users usrs = groupFile.getUsers();
        Glide.with(context).load(usrs.getHeadImg()).diskCacheStrategy(DiskCacheStrategy.ALL)  .centerCrop().into(holder.mFileDownloadHead);
        holder.mFileDownloadName.setText(usrs.getName());
        holder.mFileDownloadCount.setText("共个"+  groupFile.getUrlList().size() +"文件");
        holder.mFileDownloadCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PopItemUtil(context,groupFile.getUrlList()).setOnPopItemClick(new PopItemUtil.PopItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //TODO:文件下载
                        String url = groupFile.getUrlList().get(position);
                        onDownLoad(url);
                        ToastUtil.showToast(context,"正在下载" + groupFile.getUrlList().get(position));
                    }
                });
            }
        });
        holder.mFileDownloadDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:文件下载
                ToastUtil.showToast(context,"正在下载" + groupFile.getUrlList().size()+"个文件");
                for (int i=0;i<groupFile.getUrlList().size();i++){
                    String url = groupFile.getUrlList().get(i);
                    onDownLoad(url);
                }
            }
        });
    }

    private void onDownLoad(String url) {

        DownLoadImageService service = new DownLoadImageService(context,
                url, new ImageDownLoadCallBack() {


            @Override
            public void onDownLoadSuccess(File file) {

            }

            @Override
            public void onDownLoadSuccess(Bitmap bitmap) {
                // 在这里执行图片保存方法
                Message message = new Message();
                message.what = MSG_VISIBLE;
                handler.sendMessageDelayed(message,delayTime);

            }

            @Override
            public void onDownLoadFailed() {
                // 图片保存失败
                Message message = new Message();
                message.what = MSG_ERROR;
                handler.sendMessageDelayed(message,delayTime);
            }
        });
        //启动图片下载线程
        new Thread(service).start();
    }

    @Override
    public int getItemCount() {
        return groupFileList==null?0:groupFileList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView mFileDownloadHead;
        TextView mFileDownloadName;
        TextView mFileDownloadCount;
        ImageView mFileDownloadDown;
        public ViewHolder(View itemView) {
            super(itemView);
            mFileDownloadHead = (CircleImageView) itemView.findViewById(R.id.item_file_download_head);
            mFileDownloadName = (TextView) itemView.findViewById(R.id.item_file_download_name);
            mFileDownloadCount = (TextView) itemView.findViewById(R.id.item_file_download_count);
            mFileDownloadDown = (ImageView) itemView.findViewById(R.id.item_file_download_down);
        }
    }

    public void setOnDataChange(List<GroupFile> groupFiles){
        this.groupFileList = groupFiles;
        notifyDataSetChanged();
    }

}
