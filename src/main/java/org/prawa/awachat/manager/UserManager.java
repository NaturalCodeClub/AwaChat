package org.prawa.awachat.manager;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class UserManager {
    private static final Gson gson = new Gson();
    private static final List<UserEntry> users = new CopyOnWriteArrayList<>();
    private static final List<UserEntry> usersTemp = new CopyOnWriteArrayList<>();
    private static final File dataFolder = new File("users");
    private static final Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static final Logger logger = LogManager.getLogger();

    public static void init() {
        if (dataFolder.mkdirs()) {
            logger.info("User data folder created");
        }
        File[] userFiles = dataFolder.listFiles();
        AtomicInteger active = new AtomicInteger(0);
        for (File userFile : userFiles) {
            executor.execute(() -> {
                active.getAndIncrement();
                try {
                    FileInputStream stream = new FileInputStream(userFile);
                    byte[] data = new byte[stream.available()];
                    stream.read(data);
                    String jsonContent = new String(data);
                    try {
                        UserEntry userEntry = gson.fromJson(jsonContent, UserEntry.class);
                        users.add(userEntry);
                        usersTemp.add(userEntry);
                    } catch (Exception e) {
                        logger.error("Error in decoding user data!", e);
                    }
                    stream.close();
                } catch (Exception e) {
                    logger.error("Error in reading user data!", e);
                }finally {
                    active.getAndDecrement();
                }
            });
            logger.info("Loaded {} users",users.size());
        }
        while (active.get()>0){
            try{
                Thread.sleep(0,1);
            }catch (Exception e){
                logger.error(e.getMessage());
            }
        }
        Thread saveTimer = new Thread(()->{
            while (true){
                try{
                    Thread.sleep(10*60*1000);
                    save();
                    logger.info("Saved user data");
                }catch (Exception ignored){

                }
            }
        },"Save-Timer");
        saveTimer.setDaemon(true);
        saveTimer.start();
    }

    public static void createUser(String userName,String passWord){
        UserEntry user = new UserEntry(userName,passWord);
        usersTemp.add(user);
    }

    public static void save(){
        try{
            for (UserEntry entry : filter()){
                String fileName = entry.getUserName() + ".json";
                File userFile = new File(dataFolder,fileName);
                if (userFile.createNewFile()){
                    FileOutputStream outputStream = new FileOutputStream(userFile);
                    byte[] content = entry.getJsonData().getBytes(StandardCharsets.UTF_8);
                    outputStream.write(content);
                    outputStream.close();
                }
            }
        }catch (Exception e){
            logger.error("Error in saving user data!",e);
        }
    }

    public static List<UserEntry> filter(){
        List<UserEntry> userEntries = new CopyOnWriteArrayList<>();
        for (UserEntry user:usersTemp){
            if (!users.contains(user)){
                userEntries.add(user);
            }
        }
        return userEntries;
    }

    public static List<UserEntry> getUsers(){
        return usersTemp;
    }

}
