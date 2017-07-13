package com.app.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.thoughtworks.xstream.XStream;

import trikita.log.Log;

public class Storage {

    static String key = "SERIALIZED_STRING";
    private static Context ctx = null;

    public static Context getContext() {
        return ctx;
    }

    public static void setContext(Context ctx) {
        Storage.ctx = ctx;
    }

    public static AppConfig get() {

        Log.e(Globals.tag, "get()");
        AppConfig app = new AppConfig();

        if (ctx == null) {
            Log.e(Globals.tag, "Context Missing");
            return app;
        }

        try {
            String ret = "";
            SharedPreferences editor = ctx.getApplicationContext().getSharedPreferences(Globals.storage, 0);
            ret = editor.getString(key, "");

            XStream x = new XStream();
            Log.e(Globals.tag, ret);
            AppConfig sum = (AppConfig) x.fromXML(ret);
            app = sum;
        } catch (Exception e) {
            LogEx.print(e);
        }

        return app;
    }

    public static void save() {
        Log.e(Globals.tag, "save()");
        saveCore(Globals.app);
    }

    public static void reset() {
        Log.e(Globals.tag, "reset()");
        AppConfig app = new AppConfig();
        saveCore(app);
    }

    private static void saveCore(AppConfig app) {
        try {

            XStream x = new XStream();
            String str = x.toXML(app);
            Log.e(Globals.tag, str);

            SharedPreferences spref = ctx.getApplicationContext().getSharedPreferences(Globals.storage, 0);
            Editor editor = spref.edit();
            editor.putString(key, str);
            editor.commit();

        } catch (Exception e) {
            LogEx.print(e);
        }
    }
}
