package com.example.busapp20;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class wifiReceiver extends BroadcastReceiver {


    static String TAG = "WIFIRECEIVER";
    List<ScanResult> results;
    public int Trigger;
    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {


        //Triggered on wifi scan's result available broadcast received

        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        results = wifiManager.getScanResults();
        Log.i(TAG, "Wifi Scan Received");
        int isFound = 0;

        // Now we build the list with available networks.

        if (results.size() == 0){
            Log.i(TAG, "EMPTY WIFI LIST");
        }

        else {
            Log.i(TAG, "RECEIVED WIFI LIST");

            for (ScanResult scanResult : results) {
                arrayList.add(scanResult.SSID + " - " + scanResult.BSSID + ", " + scanResult.level);
            }

            for (ScanResult scanResult : results) {
                if (scanResult.BSSID.equals("84:80:2d:c3:a0:72") && scanResult.level > -75) {
                    isFound++;
                }
                if (scanResult.BSSID.equals("00:3a:98:7d:4a:c2") && scanResult.level > -62) {
                    isFound++;
                }
            }
            if (isFound == 2) {
                Trigger++;
            }

            if (Trigger>3){

                // function that launch the app
            }
        }
    }
}