package com.example.a731.aclass.util;

import com.example.a731.aclass.data.Commend;
import com.example.a731.aclass.data.Group;
import com.example.a731.aclass.data.News;
import com.example.a731.aclass.data.Notice;
import com.example.a731.aclass.data.Signature;
import com.example.a731.aclass.data.Users;
import com.example.a731.aclass.data.Vote;

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

    //更新用户信息
    public static void updateUser(Users user,String objectId,UpdateListener listener){
        user.update(objectId, listener);
    }

    //获取验证码
    public static void requestSMSCode(String userName, QueryListener<Integer> listener){
        BmobSMS.requestSMSCode(userName,SMS_MODEL_NAME,listener);
    }

    /*--------------------------------班圈操作----------------------------------------*/

    //创建班圈
    public static void createGroup(String groupId,String groupName, Users creator, String headImg, SaveListener<String> listener){
        Group group = new Group();
        group.setName(groupName);
        group.setGroupId(groupId);
        group.setCreator(creator);
        BmobRelation relation = new BmobRelation();
        relation.add(creator);
        group.setMember(relation);
        group.setAdministrator(relation);
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

    public static void getGroupByField(String field,String value,FindListener<Group> listener){
        BmobQuery<Group> query = new BmobQuery<>();
        query.addWhereEqualTo(field,value);
        query.include("creator");
        query.findObjects(listener);
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

    public static void queryGroupAdmin(String groupObjectId,FindListener<Users> listener) {
        BmobQuery<Users> query = new BmobQuery<>();
        Group group = new Group();
        group.setObjectId(groupObjectId);
        query.addWhereRelatedTo("administrator", new BmobPointer(group));
        query.findObjects(listener);
    }

    /*-------------------------------通知操作---------------------------------------*/
    public static void addNotice(Notice notice,SaveListener<String> listener){
        notice.save(listener);
    }

    public static void updateNotice(Notice notice, String objectId,UpdateListener listener) {
        notice.update(objectId,listener);
    }

    public static void queryNotice(String groupObjectId,FindListener<Notice> listener){
        BmobQuery<Notice> query = new BmobQuery<>();
        Group group = new Group();
        group.setObjectId(groupObjectId);
        query.addWhereEqualTo("group", new BmobPointer(group));
        query.include("creator,group.name");
        query.findObjects(listener);
    }

    /*-----------------------------------投票操作-------------------------------------------*/
    public static void addVote(Vote vote, SaveListener<String> listener){
        vote.save(listener);
    }

    public static void updateVote(Vote vote, String objectId,UpdateListener listener) {
        vote.update(objectId,listener);
    }

    public static void queryVote(String groupObjectId,FindListener<Vote> listener){
        BmobQuery<Vote> query = new BmobQuery<>();
        Group group = new Group();
        group.setObjectId(groupObjectId);
        query.addWhereEqualTo("group", new BmobPointer(group));
        query.findObjects(listener);
    }

    public static void queryVoteByObjectId(String objectId,QueryListener<Vote> listener) {
        BmobQuery<Vote> query = new BmobQuery<>();
        query.include("creator");
        query.getObject(objectId,listener);
    }

    /*-------------------------动态操作--------------------------------*/
    public static void addNews(News news, SaveListener<String> listener){
        news.save(listener);
    }

    public static void updateNews(News news, String objectId,UpdateListener listener) {
        news.update(objectId,listener);
    }

    public static void queryNews(String groupObjectId,FindListener<News> listener){
        BmobQuery<News> query = new BmobQuery<>();
        Group group = new Group();
        group.setObjectId(groupObjectId);
        query.addWhereEqualTo("group", new BmobPointer(group));
        query.include("users");
        query.findObjects(listener);
    }


    /*-------------------------------签到操作-----------------------------------*/

    public static void createSignature(Signature signature,SaveListener<String> listener) {
        signature.save(listener);
    }

    public static void updateSignature(Signature signature,String objectId,UpdateListener listener) {
        signature.update(objectId,listener);
    }

    public static void querySignature(String groupObjectId,FindListener<Signature> listener){
        BmobQuery<Signature> query = new BmobQuery<>();
        Group group = new Group();
        group.setObjectId(groupObjectId);
        query.addWhereEqualTo("group", new BmobPointer(group));
        query.findObjects(listener);
    }


    public static void querySignatureMembers(String objectId,FindListener<Users> listener) {
        BmobQuery<Users> query = new BmobQuery<>();
        Signature signature = new Signature();
        signature.setObjectId(objectId);
        query.addWhereRelatedTo("hasSign", new BmobPointer(signature));
        query.findObjects(listener);
    }

    public static void saveCommend(Commend commend,SaveListener<String> listener) {
        commend.save(listener);
    }

    public static void queryCommend(int type,String objectId,FindListener<Commend> listener) {
        BmobQuery<Commend> query = new BmobQuery<>();
        switch (type){
            case CommendPopWindowUtil.USER:
                Users users = BmobUser.getCurrentUser(Users.class);
                query.addWhereEqualTo("users",new BmobPointer(users));
                query.order("createdAt");
                query.include("users");
                query.findObjects(listener);
                break;
            case CommendPopWindowUtil.VOTE:
                Vote vote = new Vote();
                vote.setObjectId(objectId);
                query.order("createdAt");
                query.addWhereEqualTo("vote",new BmobPointer(vote));
                query.include("users");
                query.findObjects(listener);
                break;
            case CommendPopWindowUtil.NEWS:
                News news = new News();
                news.setObjectId(objectId);
                query.order("createdAt");
                query.addWhereEqualTo("news",new BmobPointer(news));
                query.include("users");
                query.findObjects(listener);
                break;
        }

    }
}