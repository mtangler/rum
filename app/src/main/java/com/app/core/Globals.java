package com.app.core;

public class Globals {

    // app globals
    public static String tag = "rum";
    public static String storage = tag + "_storage";
    public static boolean exitApp = true;

    // for storage
    public static AppConfig app = new AppConfig();

    // reset function, generally call on logout
    public static void reset() {
        app = new AppConfig();
        appExited = false;
    }

    //utility vars
    public static boolean appExited = false;

}
