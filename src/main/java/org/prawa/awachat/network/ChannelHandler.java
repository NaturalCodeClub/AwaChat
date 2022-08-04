package org.prawa.awachat.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prawa.awachat.network.codec.MessageDecoder;

public class ChannelHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger logger = LogManager.getLogger();
    public static void customRead(ChannelHandlerContext channelHandlerContext,String s){
        if(s.isEmpty()) return;
        JSONMessageHandler.onMessageReceive(s,channelHandlerContext.channel());
    }
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s){
        customRead(channelHandlerContext,s);
    }
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx){
        logger.info("Channel disconnected:"+ctx.channel());
        JSONMessageHandler.onChannelDisconnect(ctx.channel());
        ctx.fireChannelUnregistered();
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        logger.info("Channel connected:"+ctx.channel());
        JSONMessageHandler.onChannelConnected(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error(cause);
    }
}
