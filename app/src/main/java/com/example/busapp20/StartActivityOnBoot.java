package com.example.busapp20;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.busapp20.Background.BackgroundService;

//  ******************************************************************************************** //
///       THS CLASS RECEIVES THE BOOT COMPLETED BROADCAST AND STARTS THE BACKGROUND SERVICE      //
//  ******************************************************************************************** //

public class StartActivityOnBoot extends BroadcastReceiver {

    StartActivityOnBoot() {

    }

    ///*** Receives BOOT COMPLETED INTENT and starts automatically BackgroundService

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            Toast.makeText(context, "Boot Completed", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(context, BackgroundService.class);
            context.startService(i);
        }
    }
}
