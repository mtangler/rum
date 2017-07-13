package com.app.gcmpns;


import com.app.rum.R;

public class GCMConfig {

    public static final String GOOGLE_PROJECT_ID = "PROJECT_NUMBER_HERE";

    public static final String LOG_TAG = "GCM";

    public static String titleKey = "message";
    public static String descKey = "description";
    public static String noty_id = "noty_id";
    public static int smallIcon = R.mipmap.ic_launcher;

    // auto increment every time new notification arrives
    public static int NOTIFICATION_ID = 1;
}
