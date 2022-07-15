package org.prawa.awachat.manager;

public class ConfigEntry {
    public final String serverIp;
    public final int serverPort;
    public final long messageSpeedLimitMS;
    public ConfigEntry(){
        this.serverIp = "0.0.0.0";
        this.serverPort = 2344;
        this.messageSpeedLimitMS = 20;
    }
}
