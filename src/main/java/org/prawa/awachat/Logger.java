package org.prawa.awachat;

import java.util.Date;

public class Logger {
    public static enum Level{
        INFO,
        DEBUG,
        ERROR,
        FATAL,
        WARNING
    }
    public static void log(Level l,String message) {
        System.out.println("["+new Date()+"]"+"["+Thread.currentThread().getName()+"]"+"["+l+"] " + message);
    }
}
