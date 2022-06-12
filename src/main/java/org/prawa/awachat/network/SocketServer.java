package org.prawa.awachat.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketServer {
    private static final Logger logger = LogManager.getLogger();
    private final AtomicInteger workerId = new AtomicInteger(0);
    public void runServer(String host ,int port) {
        Thread serverThread = new Thread(()->{
            NioEventLoopGroup group = new NioEventLoopGroup(r->{
                Thread thread = new Thread(r,"Netty Server IO #"+workerId.getAndIncrement());
                thread.setDaemon(true);
                return thread;
            });
            NioEventLoopGroup group2 = new NioEventLoopGroup(r->{
                Thread thread = new Thread(r,"Netty Server IO #"+workerId.getAndIncrement());
                thread.setDaemon(true);
                return thread;
            });
            try {
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(group, group2)
                        .childOption(ChannelOption.SO_KEEPALIVE, true)
                        .childOption(ChannelOption.TCP_NODELAY, true)
                        .channel(NioServerSocketChannel.class)
                        .childHandler(new ChannelInitializer<Channel>() {
                            @Override
                            protected void initChannel(Channel channel){
                                channel.pipeline()
                                        .addLast("decoder",new StringDecoder(StandardCharsets.UTF_8))
                                        .addLast("encoder",new StringEncoder(StandardCharsets.UTF_8))
                                        .addLast("handler",new ChannelHandler());
                            }
                        });
                logger.info("Server started at {}:{}",InetAddress.getLocalHost().getHostAddress(), port);
                bootstrap.bind(port).sync();
            }catch (Exception e) {
                e.printStackTrace();
            }
        },"AwaChatServer-Thread");
        serverThread.setDaemon(true);
        serverThread.start();
        try{
            while(true){
                Thread.sleep(0,1);
            }
        }catch (Exception e){
            logger.error("Error in waiting server!",e);
        }
    }
}
