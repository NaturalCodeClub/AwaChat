package org.prawa.awachat.server.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import org.prawa.awachat.Logger;
import java.io.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerHandler {
    public static JSONObject users = new JSONObject();
    private static File file = new File("users.json");
    private static ScheduledThreadPoolExecutor timer;
    public static void init() throws IOException {
        initConfig();
        Logger.log(Logger.Level.INFO, "Loading users");
        if(!file.exists()) {
            file.createNewFile();
        }
        FileInputStream in = new FileInputStream(file);
        byte[] bytes = new byte[in.available()];
        in.read(bytes);
        String json = new String(bytes);
        users = JSONObject.parseObject(json);
        if(users == null) {
            users = new JSONObject();
        }
        Logger.log(Logger.Level.INFO, "Loaded users");
        Logger.log(Logger.Level.INFO, "Starting timer");
        timer = new ScheduledThreadPoolExecutor(1);
        timer.scheduleAtFixedRate(() -> {
            try {
                FileWriter fw = new FileWriter(file);
                fw.write(users.toJSONString());
                fw.flush();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        },0,10, TimeUnit.MINUTES);
        Logger.log(Logger.Level.INFO, "Started timer");
    }
    public static JSONObject config = new JSONObject();
    public static void initConfig(){
        try {
            Logger.log(Logger.Level.INFO, "Loading config");
            File f = new File("config.json");
            if(!f.exists()) {
                f.createNewFile();
            }
            FileInputStream in = new FileInputStream(f);
            byte[] bytes = new byte[in.available()];
            in.read(bytes);
            String json = new String(bytes);
            config = JSONObject.parseObject(json);
            if(config == null) {
                config = new JSONObject();
                config.put("port", 8088);
                config.put("host", "0.0.0.0");
                FileWriter fw = new FileWriter(f);
                fw.write(config.toJSONString());
                fw.flush();
                fw.close();
            }
            Logger.log(Logger.Level.INFO, "Loaded config");
        } catch (IOException e) {
            Logger.log(Logger.Level.ERROR, "Error loading config"+e.getMessage());
        }
    }
    public static void shutdown() {
        timer.shutdown();
        try {
            Logger.log(Logger.Level.INFO, "Saving users");
            FileWriter fw = new FileWriter(file);
            fw.write(users.toJSONString());
            Logger.log(Logger.Level.INFO, "Saved users");
        } catch (IOException e) {
            Logger.log(Logger.Level.ERROR, "Error saving users"+e.getMessage());
        }
    }

}
