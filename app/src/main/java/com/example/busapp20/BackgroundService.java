package com.example.busapp20;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;


public class BackgroundService extends Service {

    public static final String CHANNEL_ID = "BusAppServiceChannel";
    static WifiManager wifiManager;

    @Override
    public void onCreate() {
        super.onCreate();

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);


        // Visualizzo un Toast su schermo per avvisare l'utente dell'avvenuta
        // creazione del servizio
        Toast.makeText(this, "Service Created\nBackgoundService onCreate", Toast.LENGTH_LONG)
                .show();
        }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String input = "The service is running";
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("BusApp Background Service")
                .setContentText(input)
                .setSmallIcon(R.mipmap.ic_stat_airport_shuttle)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        // Visualizzo un Toast su schermo per avvisare l'utente dell'avvenuta
        // inizializzazione del servizio.
        Toast.makeText(this, "Service Started\nBackgroundService onStart", Toast.LENGTH_LONG).show();

        return START_STICKY;
    }




    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "BusApp Background Service",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Service STOPPED\nBackgroundService onDestroy", Toast.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
