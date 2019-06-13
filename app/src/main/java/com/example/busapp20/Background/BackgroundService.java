package com.example.busapp20.Background;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import android.widget.Toast;

import com.example.busapp20.MainActivity;
import com.example.busapp20.R;

public class BackgroundService extends Service {

    private static final String CHANNEL_ID = "BusAppServiceChannel";
    public static WifiManager wifiManager;

    @Override
    public void onCreate() {

        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);

        super.onCreate();
        WifiLoopTimer.start(wifiManager, this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("BusApp Autoticket Service")
                .setContentText("The service is running")
                .setSmallIcon(R.drawable.ic_stat_persistentnotification)
                .setColor(Color.parseColor("#FF5722"))
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);


        // Visualizzo un Toast su schermo per avvisare l'utente dell'avvenuta
        // inizializzazione del servizio.
        Toast.makeText(this, "Autoticket Service Started", Toast.LENGTH_SHORT).show();

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

        Toast.makeText(this, "Autoticket Service STOPPED", Toast.LENGTH_LONG).show();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
