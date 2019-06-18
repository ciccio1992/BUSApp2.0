package com.example.busapp20.Background;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;


//  ****************************************************************************************** //
/// *** THIS CLASS HANDLES THE BACKGROUND SERVICE THAT KEEPS THE APP WORKING IN BACKGROUND *** //
//  ****************************************************************************************** //

public class BackgroundService extends Service {

    private static final String CHANNEL_ID = "BusAppBackgroundServiceChannel";
    private static final int ID = 1;

    public static WifiManager wifiManager;
    public static boolean notificationSent = false;
    private static Timer notificationTimer;
    BroadcastReceiver myReceiver = null;

    public static void setNotificationDelay() {

        notificationSent = true;

        if (notificationTimer == null) {
            notificationTimer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    notificationSent = false;
                    Log.i("BACKGROUND SERVICE", "Notification delay finished.");

                }
            };
            notificationTimer.schedule(timerTask, 1800000);
            Log.i("BACKGROUND SERVICE", "Notification delay started.");
        }
    }

    @Override
    public void onCreate() {

        wifiManager = (WifiManager) this.getApplicationContext().getSystemService(WIFI_SERVICE);

        super.onCreate();
        WifiLoopTimer.start(wifiManager, this);

        myReceiver = new wifiReceiver();
        registerReceiver(myReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        ///*** Here we used a persistent notification and a Toast to observe the life of this service
        ///*** Used for development

        /*
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("BusApp Autoticket Service")
                .setContentText("The service is running")
                .setSmallIcon(R.drawable.ic_stat_persistentnotification)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setColor(Color.parseColor("#FF5722"))
                .setContentIntent(pendingIntent)
                .build();

        startForeground(ID, notification);


        Toast.makeText(this, "Autoticket Service Started", Toast.LENGTH_SHORT).show();

        */

        return START_STICKY;
    }

    ///*** Used to start the notification. Used for development.
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


    ///*** Used for development
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
