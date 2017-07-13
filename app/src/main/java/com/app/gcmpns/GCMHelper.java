package com.app.gcmpns;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import trikita.log.Log;

public class GCMHelper {

    Activity activity;
    GoogleCloudMessaging gcm;

    String regId, mainActivityName;
    public Context context;
    public static final String REG_ID = "regId";
    private static final String APP_VERSION = "appVersion";

    public GCMHelper(Activity activity, String mainActivityName) {
        this.activity = activity;
        context = activity.getApplicationContext();
        this.mainActivityName = mainActivityName;
    }

    public String registerGCM(NotyCallback noty) {
        gcm = GoogleCloudMessaging.getInstance(activity);
        regId = getFromSP(context);

        if (TextUtils.isEmpty(regId)) {
            registerInBackground(noty);
            Log.d(GCMConfig.LOG_TAG, "registerGCM - successfully registered with GCM Server. :\n" + regId);
        } else {
            noty.runRegistationID(regId);
            Log.d(GCMConfig.LOG_TAG, "registerGCM else{}");
        }

        return regId;
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            Log.d(GCMConfig.LOG_TAG, "I never expect this! Going down, goind down!" + e);
            throw new RuntimeException(e);
        }
    }

    private void registerInBackground(final NotyCallback noty) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regId = gcm.register(GCMConfig.GOOGLE_PROJECT_ID);
                    Log.d(GCMConfig.LOG_TAG, "registerInBackground - RegId :" + regId);
                    msg = "Device Registered, Registration Id : " + regId;
                    setInSP(context, regId);
                } catch (Exception e) {
                    msg = "Error : " + e.getMessage();
                    noty.runRegistationID("null");
                    Log.d(GCMConfig.LOG_TAG, "Error : " + msg);
                    Log.d(GCMConfig.LOG_TAG, "AsyncTask completed: " + msg);
                }

                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d(GCMConfig.LOG_TAG, "onPostExecute : " + msg);
                noty.runRegistationID(regId);
            }

        }.execute(null, null, null);
    }

    public String getFromSP(Context context) {
        final SharedPreferences prefs = activity.getSharedPreferences(mainActivityName, Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");

        if (registrationId.trim().length() == 0) {
            Log.d(GCMConfig.LOG_TAG, "Registration not found.");
            return "";
        }

        int registeredVersion = prefs.getInt(APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.d(GCMConfig.LOG_TAG, "App version changed.");
            return "";
        }

        Log.d(GCMConfig.LOG_TAG, "" + registrationId);
        return registrationId;
    }

    protected void setInSP(Context context, String regId) {
        final SharedPreferences prefs = activity.getSharedPreferences(mainActivityName, Context.MODE_PRIVATE);
        int appVersion = getAppVersion(context);
        Log.d(GCMConfig.LOG_TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putInt(APP_VERSION, appVersion);
        editor.commit();
    }
}
