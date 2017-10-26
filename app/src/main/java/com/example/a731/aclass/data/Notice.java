package com.example.a731.aclass.data;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2017/10/2/002.
 */

public class Notice extends BmobObject{
    private Users creator;//创建者
    private String title;//标题
    public Users getCreator() {
        return creator;
    }
    public void setCreator(Users creator) {
        this.creator = creator;
    }
    private String content;//内容
    private List<String> photoList;//图片链接
    private Group group;

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
