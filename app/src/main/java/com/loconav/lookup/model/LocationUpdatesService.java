package com.loconav.lookup.model;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.loconav.lookup.LandingActivity;
import com.loconav.lookup.R;
import com.loconav.lookup.service.BaseService;

public class LocationUpdatesService extends BaseService {
    private static final String TAG = LocationUpdatesService.class.getSimpleName();
    public static final String CHANNEL_ID = "LocationServiceChannel";
    private Notification notification;
    private int Notification_ID = 12;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        
        createNotificationChannel();
        startForeground(Notification_ID, getNotification());
        return START_STICKY;
    }

    private Notification getNotification() {
        Intent intent = new Intent(this, LandingActivity.class);


        // The PendingIntent to launch activity.
           PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0,
                 intent, 0);

        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText(getString(R.string.notification_text))
                .setContentTitle(getString(R.string.notification_title))
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.app_icon)
                .build();
        return notification;
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Location Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
