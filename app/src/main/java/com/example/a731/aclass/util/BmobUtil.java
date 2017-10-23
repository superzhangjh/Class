package com.example.a731.aclass.util;

import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.Users;
import java.io.File;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by 郑选辉 on 2017/9/27.
 */

public class BmobUtil {

    public static final String SMS_MODEL_NAME = "班圈App注册验证";


    //登录功能
    public static void Login(String phoneNum,String password,LogInListener<Users> listener){
        BmobUser.loginByAccount(phoneNum, password, listener);
    }
    //注册功能
    public static void register(String userName, String mobilePhoneNumber, String password, String smsCode, SaveListener<Users> listener){
        Users user = new Users();
        user.setMobilePhoneNumber(mobilePhoneNumber);
        user.setUsername(userName);
        user.setPassword(password);
        user.signOrLogin(smsCode,listener);
    }
    //查询用户
    public static void queryUser(String userName, FindListener<Users> listener){
        BmobQuery<Users> query = new BmobQuery<>();
        query.addWhereEqualTo("username",userName);
        query.findObjects(listener);
    }

    //获取验证码
    public static void requestSMSCode(String userName, QueryListener<Integer> listener){
        BmobSMS.requestSMSCode(userName,SMS_MODEL_NAME,listener);
    }

    //创建班圈
    public static void createGroup(String groupId,String groupName, Users creator, String headImg, SaveListener<String> listener){
        Group group = new Group();
        group.setName(groupName);
        group.setGroupId(groupId);
        group.setCreator(creator);
        BmobRelation relation = new BmobRelation();
        relation.add(creator);
        group.setMember(relation);
        group.setHeadImg(headImg);
        group.save(listener);
    }

    //更新班圈信息
    public static void updateGroup(String groupId, Group group, UpdateListener listener){
        group.update(groupId,listener);
    }

    /*
    * 加入班圈
    *
    * Users user = BmobUser.getCurrentUser(MyUser.class);
        Group group = new Group();
        group.setObjectId("ESIt3334");
        BmobRelation relation = new BmobRelation();
        relation.add(user);
        group.setMembers(relation);
        group.update(new UpdateListener() {
        @Override
        public void done(BmobException e) {
            if(e==null){
                Log.i("bmob","多对多关联添加成功");
            }else{
                Log.i("bmob","失败："+e.getMessage());
            }
        }

    });
    * */

    public static void getGroupByField(String field,String groupId,FindListener<Group> listener){
        BmobQuery<Group> query = new BmobQuery<>();
        query.addWhereEqualTo(field,groupId);
        query.findObjects(listener);
    }

    //上传文件
    public static BmobFile uploadBlock(String filePath, UploadFileListener listener){
        BmobFile bmobFile = new BmobFile(new File(filePath));
        bmobFile.uploadblock(listener);
        bmobFile.getFileUrl();
        return bmobFile;
    }

    public static void logOut() {
        BmobUser.logOut();
    }


    //查询群成员
    public static void queryGroupMember(String groupObjectId,FindListener<Users> listener) {
        BmobQuery<Users> query = new BmobQuery<>();
        Group group = new Group();
        group.setObjectId(groupObjectId);
        query.addWhereRelatedTo("member", new BmobPointer(group));
        query.findObjects(listener);
    }
}
