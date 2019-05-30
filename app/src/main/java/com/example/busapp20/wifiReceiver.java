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

import static com.example.busapp20.MainActivity.lastResultsRes;
import static com.example.busapp20.MainActivity.onbusres;


public class wifiReceiver extends BroadcastReceiver {


    static String TAG = "WIFIRECEIVER";
    List<ScanResult> results;
    public int Trigger;
    ArrayList<String> arrayList = new ArrayList<>();
    public static boolean onBus = false;
    int lastResults[] = new int[10];
    String resultsToString = "";
    int counter = 0;            // Counts the position where to write next result in the Array


    @Override
    public void onReceive(Context context, Intent intent) {


        //Triggered on wifi scan's result available broadcast received

        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        results = wifiManager.getScanResults();
        Log.i(TAG, "Wifi Scan Received");
        int isFound = 0;

        // This is to count the number of successful Wi-Fi scans.
        int successCounter = 0;     // Number of Success (2) in the last 10 attempts.

        // Now we build the list with available networks.

        if (results.size() == 0) {
            Log.i(TAG, "EMPTY WIFI LIST");
        } else {
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

                lastResults [counter] = isFound;
                isFound = 0;
                resultsToString = "";

                for (int i = 0; i < 10; i++) {
                    if (lastResults[i] == 2)
                        resultsToString.concat (String.valueOf(lastResults[i]) + " ");
                        successCounter++;
                }

                if (successCounter > 7)       // 7 out of 10 attempts validated
                {
                    // CODE TO HANDLE YOU ARE ON THE BUS
                    onBus = true;
                }
                else
                    onBus = false;

                if (counter == 9)           //Counter RESET to simulate queue.
                    counter = 0;

                lastResultsRes.setText (resultsToString);
                onbusres.setText(String.valueOf(onBus));
            }
        }
    }
}