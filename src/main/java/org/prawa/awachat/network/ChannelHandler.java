package org.prawa.awachat.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ChannelHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        JSONMessageHandler.onMessageReceive(s,channelHandlerContext.channel());
    }
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        JSONMessageHandler.onChannelDisconnect(ctx.channel());
        ctx.fireChannelUnregistered();
    }
}
