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
import org.prawa.awachat.manager.CommandProcessor;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketServer {
    private static final Logger logger = LogManager.getLogger();
    private final AtomicInteger workerId = new AtomicInteger(0);
    public void runServer(String host ,int port) throws InterruptedException {
        Thread serverThread = new Thread(()->{
            ThreadFactory fac = r->{
                Thread thread = new Thread(r,"Netty Server IO #"+workerId.getAndIncrement());
                thread.setDaemon(true);
                return thread;
            };
            NioEventLoopGroup group = new NioEventLoopGroup(fac);
            NioEventLoopGroup group2 = new NioEventLoopGroup(fac);
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
                bootstrap.bind(port).sync();
                logger.info("Server started at {}:{}.Try to input help to get help!",InetAddress.getLocalHost().getHostAddress(), port);
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNext()){
                    String command = scanner.nextLine();
                    CommandProcessor.handleCommand(command);
                }

            }catch (Exception e) {
                e.printStackTrace();
            }
        },"AwaChatServer-Thread");
        serverThread.setDaemon(true);
        serverThread.start();
        serverThread.join();
    }
}
