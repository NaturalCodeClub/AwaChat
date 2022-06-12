package org.prawa.awachat;


import org.prawa.awachat.manager.ConfigManager;
import org.prawa.awachat.manager.UserManager;
import org.prawa.awachat.network.SocketServer;

public class Main {
    public static void main(String[] args) {
        UserManager.init();
        ConfigManager.init();
        SocketServer server = new SocketServer();
        server.runServer(ConfigManager.config.serverIp,ConfigManager.config.serverPort);
    }
}
