package com.app.gcmpns;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.app.activity.SplashActivity;
import com.app.core.LogEx;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import trikita.log.Log;

public class GCMNotyListener extends IntentService {

    private NotificationManager nMan;
    NotificationCompat.Builder builder;

    public GCMNotyListener() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {

                showNotification(extras, false);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                showNotification(extras, false);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                showNotification(extras, true);
            }

            GCMBrodcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void showNotification(Bundle extras, boolean success) {

        try {

            if (success) {

                String title = extras.getString(GCMConfig.titleKey);
                String desc = extras.getString(GCMConfig.descKey);
                int nid = Integer.parseInt(extras.getString(GCMConfig.noty_id));

                nMan = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

                Intent intent = new Intent(this, SplashActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, 0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                Notification notification = builder.setContentIntent(contentIntent).setSmallIcon(GCMConfig.smallIcon)
                        .setTicker(title).setWhen(0).setAutoCancel(true).setContentTitle(title).setSound(soundUri)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(desc)).setContentText(desc).build();

                builder.setContentIntent(contentIntent);
                nMan.notify(nid, notification);
                Log.d(GCMConfig.LOG_TAG, "Notification " + nid + " " + title);

            } else {
                Log.d(GCMConfig.LOG_TAG, "Some error occured or message got deleted.");
            }
        } catch (Exception e) {
            LogEx.print(e);
        }
    }
}
