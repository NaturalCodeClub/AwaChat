package org.prawa.awachat.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prawa.awachat.manager.CommandProcessor;
import org.prawa.awachat.manager.ConfigManager;
import org.prawa.awachat.manager.UserManager;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
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
                                        .addLast(new LengthFieldBasedFrameDecoder(32768, 0, 2, 0, 2))
                                        .addLast(new LengthFieldPrepender(2))
                                        .addLast(new StringDecoder(StandardCharsets.UTF_8))
                                        .addLast(new StringEncoder(StandardCharsets.UTF_8))
                                        .addLast(new ChannelHandler());
                            }
                        });
                bootstrap.bind(new InetSocketAddress(host,port)).sync();
                logger.info("Server started at {}:{}.Try to input help to get help!",InetAddress.getLocalHost().getHostAddress(), port);
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNext()){
                    String command = scanner.nextLine();
                    if (command.equals("stop")){
                        break;
                    }
                    CommandProcessor.handleCommand(command);
                }
                onServerStop();
            }catch (Exception e) {
                e.printStackTrace();
            }
        },"AwaChatServer-Thread");
        serverThread.setDaemon(true);
        serverThread.start();
        serverThread.join();
    }
    public static void onServerStop(){
        logger.info("Stopping saver");
        UserManager.currentDataSaver.interrupt();
        logger.info("Saving user data and loader thread");
        UserManager.save();
        UserManager.executor.shutdownNow();
        System.exit(0);
    }
}
