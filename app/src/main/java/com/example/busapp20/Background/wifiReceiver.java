package com.example.busapp20.Background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.example.busapp20.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Integer.valueOf;


//  ******************************************************************************************* //
/// ********** This class is a Broadcast receiver for automatic wifi scans.                     //
///            This same class reports and analyzes results looking for onbus==true!  ********* //
//  ******************************************************************************************* //
///
///
public class wifiReceiver extends BroadcastReceiver {


    static String TAG = "WIFIRECEIVER";
    List<ScanResult> results;
    public static boolean onBus = false;
    public static int[] lastResults = new int[10];
    public static String resultsToString = "";
    public static ArrayList<String> arrayList = null;

    int counter = 0;            // Counts the position where to write next result in the Array


    @Override

    /// onReceive WiFi SCAN RESULTS are processed through an algorithm to defyne if we are onBus.
    public void onReceive(Context context, Intent intent) {


        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        boolean autoticket = sharedPreferences.getBoolean("autoticket", false);
        boolean autoopen = sharedPreferences.getBoolean("autoopen", true);

        int sensibility = valueOf(Objects.requireNonNull(sharedPreferences.getString("level", "5")));


        /// Triggered on wifi scan's result available broadcast received

        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        results = wifiManager.getScanResults();
        arrayList = new ArrayList<>();
        int isFound = 0;

        // Counts the number of successful Wi-Fi scans.

        int successCounter = 0;     // Number of Success (2) in the last 10 attempts.

        /// We build a list with available networks.

        if (results.size() == 0) {
            Log.i(TAG, "EMPTY WIFI LIST");
        } else {
            Log.i(TAG, "RECEIVED WIFI LIST");

            for (ScanResult scanResult : results) {
                arrayList.add(scanResult.SSID + " - " + scanResult.BSSID + ", " + scanResult.level);
            }

            for (ScanResult scanResult : results) {

                /// Allowed MAC ADDRESSES ARE WRITTEN IN HERE!!

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

            /// Eventually YOU ARE ON THE BUS IF YOU RECEIVE ACCESS POINTS WITH ENOUGH STRENGTH.
            /// Every strong AP increments int isFound. If isFound == 2 (Two Strong APs for
            /// n == sensibilty times, YOU ARE DEFINITELY ON THE BUS.

            lastResults[counter] = isFound;
            counter++;

            resultsToString = "";
            for (int i = 0; i < 10; i++) {
                if (lastResults[i] == 2) {
                    successCounter++;
                }
            }


            // (sensibility) out of 10 attempts means onBus is validated.

            /// IN HERE GOES THE CODE TO HANDLE YOU ARE MOVING ON THE BUS
            if (successCounter >= sensibility && !onBus) {

                // fronte di salita

                onBus = true;
                //Toast.makeText(context, "YOU ARE ON THE BUS", Toast.LENGTH_SHORT).show();


                if (autoopen) { // We give input to the app to launch main activity
                    PackageManager pm = context.getPackageManager();
                    String packageName = "com.example.busapp20";

                    Intent i = pm.getLaunchIntentForPackage(packageName);
                    if (i != null) {
                        context.startActivity(i);
                    } else {
                        Log.i(TAG, "Error opening the app automatically");
                    }
                } else {

                    Intent i = new Intent(context, OnbusNotificationService.class);
                    context.startService(i);
                }


                if (autoticket) {
                    MainActivity.BuyTicketAlertDialogVersion(context);
                }

            } else if (successCounter <= sensibility && onBus) {
                onBus = false;
            }

            if (counter == 10) {           //Counter RESET to simulate queue.
                counter = 0;
            }
        }
    }


}

