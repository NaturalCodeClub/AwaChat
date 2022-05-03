package org.prawa.awachat.server.friendmanager;

import java.io.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Manager {
    public static ConcurrentHashMap<String, List<String>> friends = new ConcurrentHashMap<>();
    private static ScheduledThreadPoolExecutor timer = new ScheduledThreadPoolExecutor(1);
    private static final File database = new File("database.dat");
    public static void init(){
        try {
            if (!database.exists()) {
                database.createNewFile();
                ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(database));
                os.writeObject(friends);
                os.close();
            }else {
                ObjectInputStream os = new ObjectInputStream(new FileInputStream(database));
                friends = (ConcurrentHashMap<String, List<String>>) os.readObject();
                os.close();
            }
            timer.scheduleAtFixedRate(()->{
                try {
                    ObjectOutputStream os1 = new ObjectOutputStream(new FileOutputStream(database));
                    os1.writeObject(friends);
                    os1.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            },0,5, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void shutdown(){
        timer.shutdown();
        try {
            ObjectOutputStream os1 = new ObjectOutputStream(new FileOutputStream(database));
            os1.writeObject(friends);
            os1.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
