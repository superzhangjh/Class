package com.example.a731.aclass.util;

import android.text.TextUtils;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 郑选辉 on 2017/10/4.
 */

public class EaseMobUtil {
    public static final int CHATTYPE_PERSONAL = 1001;
    public static final int CHATTYPE_GROUP = 1002;

    public static final int TYPE_SEND_MESSAGE = 1003;
    public static final int TYPE_GET_MESSAGE = 1004;
    public static final int MODIFIED_RESULT = 1005;

    public static final int TYPE_SINGLE_SELECT = 1006;
    public static final int TYPE_DOUBLE_SELECT = 1007;

    /*------------------基础功能---------------------*/
    //注册功能
    public static void signUp(String username,String password) throws HyphenateException {
        EMClient.getInstance().createAccount(username,password);
    }

    //登录功能
    public static void login(String username, String passsword, EMCallBack callBack){
        EMClient.getInstance().login(username,passsword,callBack);
    }

    //退出登录
    public static void logout(EMCallBack callBack){
        EMClient.getInstance().logout(true,callBack);
    }

    //监听连接状态
    public static void onConnectingListener(EMConnectionListener listener){
        EMClient.getInstance().addConnectionListener(listener);
    }

    //登录后读取本地回话和群组
    public static void onLoad(){
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();

    }

    /*---------------聊天信息功能------------------*/

    //获取所有会话
    public static Map<String, EMConversation> getAllConversations(){
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        return conversations;

    }

    //发送文本消息
    public static void sendTextMessage(EMMessage message,EMCallBack callBack){
        message.setMessageStatusCallback(callBack);
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    //发送图片消息
    public static void sendImageMessage(String imagePath,String toChatUsername,int chatType){
        //imagePath为图片本地路径，false为不发送原图（默认超过100k的图片会压缩后发给对方），需要发送原图传true
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
        //如果是群聊，设置chattype，默认是单聊
        if (chatType == CHATTYPE_GROUP)
            message.setChatType(EMMessage.ChatType.GroupChat);
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    public static EMMessage getConversationLastRecord(String username){
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        //获取此会话的所有消息
        if (conversation == null){
            return null;
        }
        EMMessage messages = conversation.getLastMessage();
        //SDK初始化加载的聊天记录为20条，到顶时需要去DB里获取更多
        //获取startMsgId之前的pagesize条消息，此方法获取的messages SDK会自动存入到此会话中，APP中无需再次把获取到的messages添加到会话中
        //List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId, pagesize);
        return  messages;
    }

    public static List<EMMessage> getConversationRecord(String username, String startMsgId){
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(username);
        List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId, 20);
        return messages;
    }

    public static EMConversation getConversation(String username) {
        return EMClient.getInstance().chatManager().getConversation(username);
    }

    /*-------------------好友功能----------------------------*/
    //获取好友列表
    public static List<String> getFriends() throws HyphenateException {
        return EMClient.getInstance().contactManager().getAllContactsFromServer();
    }

    //添加好友
    public static void addFriendd(String username,String reason) throws HyphenateException {
        EMClient.getInstance().contactManager().addContact(username,reason);
    }

    //删除好友
    public static void deleteFriend(String username) throws HyphenateException {
        EMClient.getInstance().contactManager().deleteContact(username);
    }

    //同意好友请求
    public static void acceptFriendRequest(String username) throws HyphenateException {
        EMClient.getInstance().contactManager().acceptInvitation(username);
    }

    //拒绝好友请求
    public static void refuseFriendRequest(String username) throws HyphenateException {
        EMClient.getInstance().contactManager().declineInvitation(username);
    }

        /*-----------------群组功能--------------------------------*/

    //创建班圈
    public static EMGroup createGroup(String groupName) throws HyphenateException {
        EMGroupOptions option = new EMGroupOptions();
        option.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
        EMGroup group = EMClient.getInstance().groupManager().createGroup(groupName,null,new String[]{},null,option);
        return group;
    }

    //添加管理员权限
    public static void addGroupAdmin(String groupId,String admin) throws HyphenateException {
        EMClient.getInstance().groupManager().addGroupAdmin(groupId,admin);
    }

    //移除管理员权限
    public static void removeGroupAdmin(String groupId,String admin) throws HyphenateException {
        EMClient.getInstance().groupManager().removeGroupAdmin(groupId, admin);
    }

    //群主加人
    public static void addUserToGroup(String groupId,String[] newmembers) throws HyphenateException {
        EMClient.getInstance().groupManager().addUsersToGroup(groupId,newmembers);
    }

    //群员邀请
    public static void inviteUser(String groupId,String[] newmembers) throws HyphenateException {
        EMClient.getInstance().groupManager().inviteUser(groupId,newmembers,null);
    }

    //搜索加入某个班圈
    public static void applyJoinToGroup(String groupId,String reason) throws HyphenateException {
        EMClient.getInstance().groupManager().applyJoinToGroup(groupId, reason);
    }

    //群组踢人
    public static void removeUserFromGroup(String groupId,String username) throws HyphenateException {
        EMClient.getInstance().groupManager().removeUserFromGroup(groupId, username);
    }

    //退出群组
    public static void leaveGroup(String groupId) throws HyphenateException {
        EMClient.getInstance().groupManager().leaveGroup(groupId);
    }

    //解散群组
    public static void destoryGroup(String groupId) throws HyphenateException {
        EMClient.getInstance().groupManager().destroyGroup(groupId);
    }

    //获取群成员列表
    public static List<String> fetchGroupMembers(String groupId) throws HyphenateException {
        List<String> memberList = new ArrayList<>();
        EMCursorResult<String> result = null;
        final int pageSize = 20;
        do {
            result = EMClient.getInstance().groupManager().fetchGroupMembers(groupId,result!=null?result.getCursor():"",pageSize);
            memberList.addAll(result.getData());
        }while(!TextUtils.isEmpty(result.getCursor()) && result.getData().size() == pageSize);
        return memberList;
    }

    //获取已加入的群组列表
    public static List<EMGroup> getAllGroup() throws HyphenateException {
        if (EMClient.getInstance().groupManager().getAllGroups()==null || EMClient.getInstance().groupManager().getAllGroups().size()==0){
            return EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
        }else{
            return EMClient.getInstance().groupManager().getAllGroups();
        }
    }

    //修改群名称
    public static void changeGroupName(String groupId,String name) throws HyphenateException {
        EMClient.getInstance().groupManager().changeGroupName(groupId, name);
    }

    //同意加群申请
    public static void acceptApplyJoinGroup(String username,String groupId) throws HyphenateException {
        EMClient.getInstance().groupManager().acceptApplication(username, groupId);
    }

    //拒绝加群申请
    public static void refuseApplyJoinGroup(String username,String groupId) throws HyphenateException {
        EMClient.getInstance().groupManager().declineApplication(username,groupId,null);
    }



}
