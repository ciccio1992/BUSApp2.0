package com.example.busapp20;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivityOnBoot extends BroadcastReceiver {


    StartActivityOnBoot() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_TIME_TICK.equals(intent.getAction())){

            Toast.makeText(context, "Boot Completed", Toast.LENGTH_SHORT).show();
            WifiLoopTimer.start();

            Intent i = new Intent(context, BackgroundService.class);
            context.startService(i);
        }
    }
}
