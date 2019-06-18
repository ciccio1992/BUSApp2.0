package com.example.busapp20.Background;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.media.audiofx.Equalizer;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.busapp20.MainActivity;
import com.example.busapp20.R;

@SuppressLint("Registered")
public class OnbusNotificationService extends BackgroundService {

    private static final String CHANNEL_ID = "BusAppTicketNotificationChannel";
    private static final int ID = 2;
    private static final long[] pattern = {500,500,500};

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_persistentnotification)
                .setContentTitle("You are on the bus.")
                .setContentText("Tap here to buy a ticket.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.parseColor("#FF5722"))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setLights(Color.YELLOW, 400, 1500)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setVibrate(pattern)
                .setOnlyAlertOnce(true)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(ID, builder.build());
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "BusApp Notification Service",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }


    }

}

