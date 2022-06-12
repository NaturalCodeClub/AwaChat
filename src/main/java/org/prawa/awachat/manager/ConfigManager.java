package org.prawa.awachat.manager;

import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class ConfigManager {
    private static final Gson gson =  new Gson();
    public static ConfigEntry config;
    private static final Logger logger = LogManager.getLogger();

    public static void init(){
        try {
            File file = new File("config.json");
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                byte[] data = new byte[fis.available()];
                fis.read(data);
                String configs = new String(data);
                config = gson.fromJson(configs, ConfigEntry.class);
                fis.close();
                return;
            }
            file.createNewFile();
            config = new ConfigEntry();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(gson.toJson(config).getBytes());
            fos.close();
        }catch (Exception e){
            logger.error("Error in creating or reading config file!",e);
        }
    }
}
