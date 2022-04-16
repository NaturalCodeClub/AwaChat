package org.prawa.awachat.server.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.prawa.awachat.Logger;

import java.util.concurrent.ConcurrentHashMap;

import static org.prawa.awachat.server.handler.ServerHandler.users;

public class ChannelHandler extends SimpleChannelInboundHandler{
    public static enum ServerMessageType {
        CHAT,
        PRIVATECHAT,
        MUSICBROADCAST,
        GOLBALMESSAGE,
    }

    public final static ConcurrentHashMap<Channel,String> channelNames = new ConcurrentHashMap<>();
    public final static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Logger.log(Logger.Level.INFO,"Channel connection established,Channel " + ctx.channel());
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        channels.remove(ctx.channel());
        channelNames.remove(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        handle(JSONObject.parseObject(o.toString()),channelHandlerContext.channel());
    }

    public JSONObject genMessage(ServerMessageType type, Object attachment) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("head", type.toString());
        jsonObject.put("body", attachment);
        return jsonObject;
    }

    public void sendChat(String message, Channel channel,String sender){
        if(sender == null) {
            sender = "Server";
        }
        if(channel != null) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("message", message);
            jsonObject1.put("sender", sender);
            JSONObject jsonObject = genMessage(ServerMessageType.PRIVATECHAT,jsonObject1);
            channel.writeAndFlush(jsonObject.toJSONString());
            return;
        }
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("message", message);
        jsonObject1.put("sender", sender);
        JSONObject jsonObject = genMessage(ServerMessageType.CHAT,jsonObject1);
        ChannelHandler.channels.writeAndFlush(jsonObject.toJSONString());
    }

    public void createUser(String username, String password){
        JSONObject user = new JSONObject();
        user.put("password", password);
        users.put(username,user);
    }

    public void handleLogin(String loginMessage, Channel channel) {
        JSONObject message = JSONObject.parseObject(loginMessage);
        String head = message.getString("head");
        if(head.equals("LOGIN")) {
            JSONObject body = message.getJSONObject("body");
            String username = body.getString("username");
            String password = body.getString("password");
            if(users.containsKey(username)) {
                JSONObject user = users.getJSONObject(username);
                if(user.getString("password").equals(password)) {
                    System.out.println("Welcome!");
                    sendChat("Welcome !" + username, channel,null);
                    ChannelHandler.channelNames.put(channel,username);
                    channels.add(channel);
                    return;
                }else {
                    sendChat("Password is wrong.",channel,null);
                }
            }else {
                sendChat("User not found", channel,null);
                return;
            }
        }
        if(head.equals("REGISTER")) {
            JSONObject body = message.getJSONObject("body");
            String username = body.getString("username");
            String password = body.getString("password");
            if(!users.containsKey(username)) {
                createUser(username,password);
                sendChat("User created!", channel,null);
                sendChat("Welcome !" + username, channel,null);
                ChannelHandler.channelNames.put(channel,username);
                channels.add(channel);
                return;
            }
            sendChat("User already exists!",channel,null);
        }
    }

    public void handle(JSONObject message, Channel channel) {
        if (message == null) {
            return;
        }
        String head = message.getString("head");
        if(head.equals("CHAT")) {
            JSONObject body = message.getJSONObject("body");
            String message1 = body.getString("message");
            sendChat(message1, null,ChannelHandler.channelNames.get(channel));
        }
        if(head.equals("LOGIN")||head.equals("REGISTER")) {
            handleLogin(message.toJSONString(), channel);
        }
    }
}
