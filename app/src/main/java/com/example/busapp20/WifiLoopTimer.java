package com.example.busapp20;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;


public class WifiLoopTimer extends BackgroundService {


    private static Timer timer;
    private static TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {

            MainActivity.wifiManager.startScan();
            Log.i("WIFILOOPTIMER", "SCAN RUN AUTOMATICALLY");
        }
    };


    public static void start() {
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(timerTask, 0, 7000);
        } else {
            return;
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
