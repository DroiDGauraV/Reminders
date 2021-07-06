package com.example.reminders;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {

    private NotificationManager mManager;

    public static final String CHANNEL1ID = "channel1id";
    public static final String CHANNEL1NAME = "channel1";

    public NotificationHelper(Context base) {
        super(base);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            createChannels();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createChannels () {
        NotificationChannel channel1 = new NotificationChannel(CHANNEL1ID, CHANNEL1NAME, NotificationManager.IMPORTANCE_HIGH);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel1);

    }

    public NotificationManager getManager () {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }
    //potential problem regarding v4 or v7 here
    public NotificationCompat.Builder getChannel1Notification (String title, String msg) {
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL1ID)
                .setContentTitle(title)
                .setContentText(msg)
                .setSmallIcon(R.drawable.ic_baseline_add_alert_24);
    }

}

