package org.prawa.awachat;

import org.prawa.awachat.server.NioSocketServer;
import org.prawa.awachat.server.friendmanager.Manager;
import org.prawa.awachat.server.handler.ServerHandler;

import java.io.IOException;
import java.util.concurrent.ThreadPoolExecutor;

public class Main {
    private static final ThreadPoolExecutor serviceExecutor = new ThreadPoolExecutor(1,Integer.MAX_VALUE,60L,java.util.concurrent.TimeUnit.SECONDS,new java.util.concurrent.ArrayBlockingQueue<Runnable>(100));
    public static void main(String[] args) throws InterruptedException, IOException {
        ServerHandler.init();
        Manager.init();
        int p = Integer.parseInt(ServerHandler.config.get("port").toString());
        serviceExecutor.execute(new NioSocketServer(p, ServerHandler.config.get("host").toString()));
        serviceExecutor.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS);
        serviceExecutor.shutdown();
        ServerHandler.shutdown();
        Manager.shutdown();
    }
}
