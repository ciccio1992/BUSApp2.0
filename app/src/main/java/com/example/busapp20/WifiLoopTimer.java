package com.example.busapp20;

import android.app.Service;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

// import static com.busappteam.busapp.MainActivity.wifiManager;

public class WifiLoopTimer extends BackgroundService {

    static WifiManager wifiManager;

    private static Timer timer;
    private static TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            wifiManager.startScan();
            Log.i("WIFILOOPTIMER","SCAN RUN AUTOMATICALLY");
        }
    };


    public static void start() {
        if (timer == null) {
            timer = new Timer();
            timer.scheduleAtFixedRate(timerTask, 0, 10000);
        } else {
            return;
        }
    }

    public void stop() {
        timer.cancel();
        timer = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
