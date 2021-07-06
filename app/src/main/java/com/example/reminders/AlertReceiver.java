package com.example.reminders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {

    String title = "";
    String msg = "";
    int notiID;

    @Override
    public void onReceive(Context context, Intent intent) {
        title = intent.getStringExtra("head");
        msg = intent.getStringExtra("msg");
        notiID = intent.getIntExtra("notiID", 0);
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannel1Notification(title, msg);
        notificationHelper.getManager().notify(notiID,nb.build());
    }
}
