package org.prawa.awachat.network;

import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prawa.awachat.manager.ConfigManager;
import org.prawa.awachat.manager.UserEntry;
import org.prawa.awachat.manager.UserManager;
import org.prawa.awachat.network.codec.JSONMessage;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class JSONMessageHandler {
    private final static Gson gson = new Gson();
    private final static AtomicInteger onlineUserCount = new AtomicInteger(0);
    private final static ConcurrentHashMap<Channel,String> channelNames = new ConcurrentHashMap<>();
    private final static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private final static Logger logger = LogManager.getLogger();
    private static final ConcurrentHashMap<Channel,Channel> channelFriendQueue = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<Channel,Long> channelLastMessageSentTime = new ConcurrentHashMap<>();

    public static void onMessageReceive(String msg, Channel channel){
        try {
            if (isOutOfLimit(channel)) {
                channel.writeAndFlush(new JSONMessage("channel",new String[]{"message_sent_too_fast"},new Object[1]).buildJson());
            }
            JSONMessage msg1 = gson.fromJson(msg, JSONMessage.class);
            handle(msg1,channel);
        }catch (Exception e){
            if (e instanceof ArrayIndexOutOfBoundsException){
                logger.warn("May the message has some wrong?");
                return;
            }
            logger.error("Error in processing message",e);
        }
    }

    private static boolean isOutOfLimit(Channel channel){
        if (!channelLastMessageSentTime.contains(channel)){
            channelLastMessageSentTime.put(channel,System.currentTimeMillis());
            return false;
        }
        if (System.currentTimeMillis() - channelLastMessageSentTime.get(channel) < ConfigManager.config.messageSpeedLimitMS){
            channelLastMessageSentTime.replace(channel,System.currentTimeMillis());
            return true;
        }
        channelLastMessageSentTime.replace(channel,System.currentTimeMillis());
        return false;
    }

    public static void onChannelDisconnect(Channel channel){
        channels.writeAndFlush(new JSONMessage("channel",new String[]{"channel_disconnected"},new Object[]{channelNames.get(channel)}).buildJson());
        channels.remove(channel);
        channelNames.remove(channel);
        onlineUserCount.getAndDecrement();
    }

    public static void onChannelConnected(Channel channel){
        onlineUserCount.getAndIncrement();
    }

    private static void handle(JSONMessage message,Channel channel){
        switch (message.getHead()){
            case "login":
                String userName = message.getData()[0].toString();
                String passWord = message.getData()[1].toString();
                handleLogin(channel,userName,passWord);
                break;
            case "register":
                String regUserName = message.getData()[0].toString();
                String regPassWord = message.getData()[1].toString();
                handleRegister(channel,regUserName,regPassWord);
                break;
            case "chat":
                String chatMessage = message.getData()[0].toString();
                String tag = message.getTags()[0];
                if (chatMessage == null){chatMessage = "";}
                if (tag == null){tag = "chat";}
                Object targetObject = message.getTags()[1];
                handleChat(channel,chatMessage,tag,targetObject);
                break;
            case "friendaddrequest":
                String targetUserName = message.getData()[0].toString();
                String leaveWord = message.getData()[1].toString();
                handleFriendRequest(channel,targetUserName,leaveWord);
            case "firendresponse":
                String source = message.getData()[0].toString();
                handleFriendResponse(source,channel);
            case "getuserinfo":
                String username = channelNames.get(channel);
                sendUserInfo(username,channel);
        }
    }

    public static boolean checkIsLogined(Channel channel){
        if (!channels.contains(channel)){
            channel.writeAndFlush(new JSONMessage("channel",new String[]{"not_login"},new Object[1]).buildJson());
            return false;
        }
        return true;
    }

    public static void handleFriendResponse(String source,Channel channel){
        if (!checkIsLogined(channel)){
            return;
        }
        if (!channelFriendQueue.contains(channel)){
            channel.writeAndFlush(new JSONMessage("channel",new String[]{"no_such_request"},new Object[1]).buildJson());
            return;
        }
        List<UserEntry> users = UserManager.getUsers();
        UserEntry sourceUser = null;
        UserEntry targetUser = null;
        for (UserEntry user : users){
            if (user.getUserName().equals(source)){
                sourceUser = user;
                user.addFriend(targetUser.getUserName());
            }
            if (user.getUserName().equals(channelNames.get(channel))){
                user.addFriend(sourceUser.getUserName());
                targetUser = user;
                UserManager.getUsers().remove(user);
                UserManager.getUsers().add(user);
            }
        }
        channelFriendQueue.remove(channel);
    }

    public static void handleFriendRequest(Channel channel,String target,String leaveWord){
        if (!checkIsLogined(channel)){
            return;
        }
        String sourceUser = channelNames.get(channel);
        JSONMessage jsonMessage = new JSONMessage("sfriendrequest",2);
        jsonMessage.setData(0,sourceUser);
        jsonMessage.setData(1,leaveWord);
        channelNames.forEach((k,v)->{
            if (v.equals(target)){
                k.writeAndFlush(jsonMessage.buildJson());
                channelFriendQueue.put(k,channel);
            }
        });
    }

    public static void sendChat(String s) {
        JSONMessage message = new JSONMessage("schat", 2);
        message.setTag(0, "chat");
        message.setData(0, s);
        message.setData(1, "severconsole");
        logger.info("[chat][{}]{}", "server", s);
        channels.writeAndFlush(message.buildJson());
    }

    public static void handleChat(Channel channel,String chatMessage,String tag,Object target){
        if (!checkIsLogined(channel)){
            return;
        }
        JSONMessage message = new JSONMessage("schat",2);
        switch (tag){
            case "private":
                if (target == null){
                    channel.writeAndFlush(new JSONMessage("channel",new String[]{"invalid_target"},new Object[1]).buildJson());
                }
                AtomicReference<Channel> targetChannel = new AtomicReference<>();
                channelNames.forEach((k,v)->{
                    if (v.equals(target)){
                        targetChannel.set(k);
                    }
                });
                if (targetChannel.get() == null){
                    channel.writeAndFlush(new JSONMessage("channel",new String[]{"target_not_found"},new Object[1]).buildJson());
                    return;
                }
                //聊天消息类型
                message.setTag(0,"private");
                //对方的名称
                message.setTag(1,channelNames.get(channel));
                UserEntry sourceChannel = UserManager.search(channelNames.get(channel));
                //是不是好友
                message.setTag(2, String.valueOf(sourceChannel.getFriends().contains(sourceChannel.getUserName())));
                logger.info("[chat][{}][{} -> {}]{}",message.getTags()[0],channelNames.get(channel),target.toString(),chatMessage);
                //具体的内容
                message.setData(0,chatMessage);
                targetChannel.get().writeAndFlush(message.buildJson());
                break;
            case "chat":
                message.setTag(0,"chat");
                //消息内容
                message.setData(0,chatMessage);
                //对方名称
                message.setData(1,channelNames.get(channel));
                logger.info("[chat][{}]{}",channelNames.get(channel),chatMessage);
                channels.writeAndFlush(message.buildJson());
                break;
        }
    }

    public static void handleLogin(Channel channel,String userName,String passWord){
        List<UserEntry> users = UserManager.getUsers();
        boolean found = false;
        boolean passed = false;
        UserEntry currentUserEntry = null;
        for (UserEntry userEntry : users){
            if (userEntry.getUserName().contains(userName)) {
                found = true;
                if (userEntry.getPassword().equals(passWord)){
                    currentUserEntry = userEntry;
                    passed = true;
                    break;
                }
            }
        }
        if (!found){
            channel.writeAndFlush(new JSONMessage("login_response",new String[]{"no_such_user"},new Object[1]).buildJson());
            return;
        }
        if (!passed){
            channel.writeAndFlush(new JSONMessage("login_response",new String[]{"wrong_password"},new Object[1]).buildJson());
            return;
        }
        channels.add(channel);
        channelNames.put(channel,userName);
        channel.writeAndFlush(new JSONMessage("login_response",new String[]{"finished"},new Object[1]).buildJson());
        JSONMessage userInfo = new JSONMessage("userinfo",1);
        userInfo.setData(0,currentUserEntry.getJsonData());
    }

    public static void sendUserInfo(String userName,Channel channel){
        JSONMessage userInfo = new JSONMessage("userinfo",1);
        userInfo.setData(0,UserManager.search(userName).getJsonData());
        channel.writeAndFlush(userInfo.buildJson());
    }

    public static void handleRegister(Channel channel, String username, String password){
        List<UserEntry> users = UserManager.getUsers();
        boolean found = false;
        for (UserEntry userEntry : users){
            if (userEntry.getUserName().contains(username)) {
                found = true;
                break;
            }
        }
        if (found){
            channel.writeAndFlush(new JSONMessage("reg_response",new String[]{"user_was_exists"},new Object[1]).buildJson());
            return;
        }
        UserManager.createUser(username,password);
        channels.add(channel);
        channelNames.put(channel,username);
        channel.writeAndFlush(new JSONMessage("reg_response",new String[]{"finished"},new Object[1]).buildJson());
    }
}
