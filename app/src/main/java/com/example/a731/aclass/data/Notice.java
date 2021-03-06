package com.example.a731.aclass.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2017/10/2/002.
 */

public class Notice extends BmobObject implements Serializable{
    private Users creator;//创建者
    private int type; //通知或图片回收
    private String title;//标题
    private String content;//内容
    private List<String> photoList;//图片链接
    private List<GroupFile> fileList;//文件链接
    private Group group;

    public List<GroupFile> getFileList() {
        return fileList;
    }

    public void setFileList(List<GroupFile> fileList) {
        this.fileList = fileList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Users getCreator() {
        return creator;
    }
    public void setCreator(Users creator) {
        this.creator = creator;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<String> photoList) {
        this.photoList = photoList;
    }
}
