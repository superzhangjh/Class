package com.example.a731.aclass.util;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;
import java.util.Map;

/**
 * Created by 郑选辉 on 2017/10/4.
 */

public class EaseMobUtil {
    public static final int CHATTYPE_PERSONAL = 1001;
    public static final int CHATTYPE_GROUP = 1002;
    public static final int CHATTYPE_GROUP_ROOM = 1003;

    public static final int TYPE_SEND_MESSAGE = 1011;
    public static final int TYPE_GET_MESSAGE = 1012;


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
    public static void getAllConversations(){
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();

    }

    //发送文本消息
    public static void sendTextMessage(String content,String toChatUsername,int chatType,EMCallBack callBack){
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        if (chatType == CHATTYPE_GROUP){
            message.setChatType(EMMessage.ChatType.GroupChat);
        }else if (chatType == CHATTYPE_GROUP_ROOM){
            message.setChatType(EMMessage.ChatType.ChatRoom);
        }

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


}
