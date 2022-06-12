package org.prawa.awachat.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChannelHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger logger = LogManager.getLogger();
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s){
        JSONMessageHandler.onMessageReceive(s,channelHandlerContext.channel());
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
    }
}
