package org.prawa.awachat.manager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.prawa.awachat.network.JSONMessageHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class CommandProcessor {
    public static final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    public static final Logger logger = LogManager.getLogger();

    public static void handleCommand(String command){
        String[] fullCommandArray = command.split(" ");
        if (fullCommandArray.length > 0){
            switch (fullCommandArray[0]){
                case "chat":
                    if (fullCommandArray.length > 1){
                        String[] args = new String[fullCommandArray.length -1];
                        System.arraycopy(fullCommandArray, 1, args, 0, fullCommandArray.length - 1);
                        StringBuilder builder = new StringBuilder();
                        for (String s : args){
                            builder.append(s).append(" ");
                        }
                        JSONMessageHandler.sendChat(builder.toString());
                        return;
                    }
                    logger.info("Invalid command args!Try chat <message>!");
                    break;
                case "help":
                    logger.info("Commands: chat");
            }
        }
    }

}
