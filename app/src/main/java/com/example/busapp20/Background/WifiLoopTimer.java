package com.example.busapp20.Background;

import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;


public class WifiLoopTimer extends BackgroundService {


    private static Timer timer;
    private static TimerTask timerTask;


    public static void start(final WifiManager wifiManager) {
        if (timer == null) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    wifiManager.startScan();
                    Log.i("WIFILOOPTIMER", "SCAN RUN AUTOMATICALLY");
                }
            };
            timer.scheduleAtFixedRate(timerTask, 0, 7000); ///INSERIRE IN SETTINGS
        }
    }


    public static void stop() {
        timer.cancel();
        timer = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
