package com.example.a731.aclass.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/11/8/008.
 */

public class GroupFile implements Serializable {
    private Users users;
    private List<String> urlList;

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }
}
