package com.app.core;

public class LogEx {

    public static boolean PRINT_STACK = true;

    public static void print(Exception e) {
        if (PRINT_STACK) {
            e.printStackTrace();
        }
    }

}
