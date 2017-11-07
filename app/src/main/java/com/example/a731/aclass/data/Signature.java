package com.example.a731.aclass.data;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by 郑选辉 on 2017/11/7.
 */

public class Signature extends BmobObject{

    private Group group;
    private String className;
    private BmobRelation hasSign;

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public BmobRelation getHasSign() {
        return hasSign;
    }

    public void setHasSign(BmobRelation hasSign) {
        this.hasSign = hasSign;
    }
}
