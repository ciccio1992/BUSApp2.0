package com.example.busapp20.Background;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Integer.valueOf;


public class WifiLoopTimer extends BackgroundService {


    private static Timer timer;


    public static void start(final WifiManager wifiManager, Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);

        int frequency = valueOf(Objects.requireNonNull(sharedPreferences.getString("frequency", "6000")));


        if (timer == null) {
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    wifiManager.startScan();
                    Log.i("WIFILOOPTIMER", "SCAN RUN AUTOMATICALLY");
                }
            };
            timer.scheduleAtFixedRate(timerTask, 0, frequency); ///INSERIRE IN SETTINGS
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
