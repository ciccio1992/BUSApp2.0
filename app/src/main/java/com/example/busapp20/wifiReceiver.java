package com.example.busapp20;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.paypal.android.sdk.cu.i;


public class wifiReceiver extends BroadcastReceiver {


    static String TAG = "WIFIRECEIVER";
    List<ScanResult> results;
    public static boolean onBus = false;
    public static int[] lastResults = new int[10];
    public static String resultsToString = "          ";
    int counter = 0;            // Counts the position where to write next result in the Array


    @Override
    public void onReceive(Context context, Intent intent) {


        //Triggered on wifi scan's result available broadcast received

        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        results = wifiManager.getScanResults();
        ArrayList<String> arrayList = new ArrayList<>();
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
                if (scanResult.BSSID.equals("00:3a:98:7d:4a:c1") && scanResult.level > -70) {
                    isFound++;
                }
                if (scanResult.BSSID.equals("00:3a:98:7d:4a:c2") && scanResult.level > -70) {
                    isFound++;
                }
                if (scanResult.BSSID.equals("1c:b0:44:12:9f:de") && scanResult.level > -70) {
                    isFound++;
                }
                if (scanResult.BSSID.equals("62:b0:44:12:9f:df") && scanResult.level > -70) {
                    isFound++;
                }
            }

            lastResults[counter] = isFound;
            counter++;

            resultsToString = "";
            for (int i = 0; i < 10; i++) {
                if (lastResults[i] == 2) {
                    successCounter++;
                }
            }

            /// 7 out of 10 attempts validated
            // MODIFIED FOR TESTING PURPOSES
            // CODE TO HANDLE YOU ARE ON THE BUS

            if (successCounter > 3) {
                onBus = true;
                Toast.makeText(context,"YOU ARE ON THE BUS", Toast.LENGTH_SHORT).show();
   //             Intent newintent = new Intent(context, MainActivity.class);
    //            context.startActivity(newintent);

            } else {
                onBus = false;
            }

            if (counter == 10) {           //Counter RESET to simulate queue.
                counter = 0;
            }


        }
    }
}