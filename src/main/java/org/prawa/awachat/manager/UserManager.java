package org.prawa.awachat.manager;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prawa.simpleutils.concurrent.forkjointasks.ParallelListTraverse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class UserManager {
    private static final Gson gson = new Gson();
    private static final Set<UserEntry> users = ConcurrentHashMap.newKeySet();
    private static final Set<UserEntry> usersTemp = ConcurrentHashMap.newKeySet();
    private static final File dataFolder = new File("users");
    public static Thread currentDataSaver;
    public static final ForkJoinPool executor = new ForkJoinPool();
    private static final Logger logger = LogManager.getLogger();

    public static void init() {
        if (dataFolder.mkdirs()) {
            logger.info("User data folder created");
        }
        logger.info("Files in user data floder:{}",dataFolder.listFiles().length);
        List<File> userFiles = Arrays.asList(dataFolder.listFiles());
        ParallelListTraverse<File> task = new ParallelListTraverse<>(userFiles,executor.getParallelism(),userFile->{
            try {
                if (!userFile.getName().endsWith(".json")){
                    logger.warn("Find a file with arg isn't json file!File name:{}",userFile.getName());
                    return;
                }
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
            }
        });
        executor.submit(task).join();
        logger.info("Loaded {} users",users.size());
        Thread saveTimer = new Thread(()->{
            while (!Thread.currentThread().isInterrupted()){
                try{
                    Thread.sleep(10*60*1000);
                    save();
                    logger.info("Saved user data");
                }catch (InterruptedException ignored){
                    break;
                }
            }
        },"Save-Timer");
        currentDataSaver = saveTimer;
        saveTimer.setDaemon(true);
        saveTimer.start();
    }

    public static void createUser(String userName,String passWord){
        UserEntry user = new UserEntry(userName,passWord);
        usersTemp.add(user);
    }

    public static UserEntry search(String username){
        return usersTemp.stream().filter(user-> user.getUserName().equals(username)).findFirst().get();
    }

    public static void save(){
        try{
            ParallelListTraverse<UserEntry> parallelListTraverse = new ParallelListTraverse<>(filter(),executor.getParallelism(),entry-> {
                try {
                    String fileName = entry.getUserName() + ".json";
                    File userFile = new File(dataFolder, fileName);
                    if (userFile.createNewFile()) {
                        FileOutputStream outputStream = new FileOutputStream(userFile);
                        byte[] content = entry.getJsonData().getBytes(StandardCharsets.UTF_8);
                        outputStream.write(content);
                        outputStream.close();
                    }
                } catch (Exception e) {
                    logger.error(e);

                }
            });
            executor.submit(parallelListTraverse).join();
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

    public static Set<UserEntry> getUsers(){
        return usersTemp;
    }

}
