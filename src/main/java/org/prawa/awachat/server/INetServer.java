package org.prawa.awachat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.prawa.awachat.Logger;

import java.net.InetAddress;

public abstract class INetServer {
    public abstract SimpleChannelInboundHandler getNewHandler();
    public void runServer(String host ,int port) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        NioEventLoopGroup group2 = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group, group2)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel channel){
                            channel.pipeline()
                                    .addLast("encoder",new StringEncoder())
                                    .addLast("decoder",new StringDecoder())
                                    .addLast("handler", getNewHandler());
                        }
                    });
            Logger.log(Logger.Level.INFO,"Server started at " + InetAddress.getLocalHost().getHostAddress() + ":" + port);
            bootstrap.bind(port).sync();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
