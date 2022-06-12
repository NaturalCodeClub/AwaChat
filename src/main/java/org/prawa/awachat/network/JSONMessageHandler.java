package org.prawa.awachat.network;

import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prawa.awachat.manager.UserEntry;
import org.prawa.awachat.manager.UserManager;
import org.prawa.awachat.network.codec.JSONMessage;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class JSONMessageHandler {
    private final static Gson gson = new Gson();
    private final static ConcurrentHashMap<Channel,String> channelNames = new ConcurrentHashMap<>();
    private final static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private final static Logger logger = LogManager.getLogger();

    public static void onMessageReceive(String msg, Channel channel){
        try {
            JSONMessage msg1 = gson.fromJson(msg, JSONMessage.class);
            handle(msg1,channel);
        }catch (Exception e){
            logger.error("Error in processing message",e);
        }
    }

    public static void onChannelDisconnect(Channel channel){
        channels.writeAndFlush(new JSONMessage("channel",new String[]{"channel_disconnected"},new Object[]{channelNames.get(channel)}).buildJson());
        channels.remove(channel);
        channelNames.remove(channel);
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
                Object targetObject = message.getData()[1];
                handleChat(channel,chatMessage,tag,targetObject);
        }
    }

    public static void handleChat(Channel channel,String chatMessage,String tag,Object target){
        if (!channels.contains(channel)){
            channel.writeAndFlush(new JSONMessage("channel",new String[]{"not_login"},new Object[1]).buildJson());
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
                message.setTag(0,"private");
                message.setTag(1,target.toString());
                message.setData(0,chatMessage);
                targetChannel.get().writeAndFlush(message.buildJson());
                break;
            case "chat":
                message.setTag(0,"chat");
                message.setData(0,chatMessage);
                channels.writeAndFlush(message.buildJson());
                break;
        }
    }

    public static void handleLogin(Channel channel,String userName,String passWord){
        List<UserEntry> users = UserManager.getUsers();
        boolean found = false;
        boolean passed = false;
        for (UserEntry userEntry : users){
            if (userEntry.getUserName().contains(userName)) {
                found = true;
                if (userEntry.getPassword().equals(passWord)){
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
