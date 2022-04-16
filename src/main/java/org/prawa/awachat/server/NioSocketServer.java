package org.prawa.awachat.server;

import io.netty.channel.SimpleChannelInboundHandler;
import org.prawa.awachat.server.handler.ChannelHandler;

public class NioSocketServer extends INetServer implements Runnable {
    private final int port;
    private final String host;
    public NioSocketServer(int port, String host) {
      this.port = port;
      this.host = host;
    }
    @Override
    public void run() {
        this.runServer(this.host,this.port);
    }
    @Override
    public SimpleChannelInboundHandler getNewHandler() {
        return new ChannelHandler();
    }
}
