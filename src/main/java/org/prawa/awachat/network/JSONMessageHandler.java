package org.prawa.awachat.network;

import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.prawa.awachat.manager.UserEntry;
import org.prawa.awachat.manager.UserManager;
import org.prawa.awachat.network.codec.JSONMessage;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class JSONMessageHandler {
    private final static Gson gson = new Gson();
    private final static ConcurrentHashMap<Channel,String> channelNames = new ConcurrentHashMap<>();
    private final static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    public static void onMessageReceive(String msg, Channel channel){
        try {
            JSONMessage msg1 = gson.fromJson(msg, JSONMessage.class);
            handle(msg1,channel);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void onChannelDisconnect(Channel channel){
        channels.writeAndFlush(new JSONMessage("channel",new String[]{"channel_disconnected"},new Object[]{channelNames.get(channel)}));
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
                handleRegsiter(channel,regUserName,regPassWord);
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
            channel.writeAndFlush(new JSONMessage("login_response",new String[]{"no_such_user"},new Object[1]));
            return;
        }
        if (!passed){
            channel.writeAndFlush(new JSONMessage("login_response",new String[]{"wrong_password"},new Object[1]));
            return;
        }
        channels.add(channel);
        channelNames.put(channel,userName);
        channel.writeAndFlush(new JSONMessage("login_response",new String[]{"finished"},new Object[1]));
    }

    public static void handleRegsiter(Channel channel,String username,String password){
        List<UserEntry> users = UserManager.getUsers();
        boolean found = false;
        for (UserEntry userEntry : users){
            if (userEntry.getUserName().contains(username)) {
                found = true;
                break;
            }
        }
        if (found){
            channel.writeAndFlush(new JSONMessage("reg_response",new String[]{"user_was_exists"},new Object[1]));
            return;
        }
        UserManager.createUser(username,password);
        channels.add(channel);
        channelNames.put(channel,username);
        channel.writeAndFlush(new JSONMessage("reg_response",new String[]{"finished"},new Object[1]));
    }
}
