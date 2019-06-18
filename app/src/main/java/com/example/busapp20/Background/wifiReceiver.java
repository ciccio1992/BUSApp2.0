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


///  ******************************************************************************************* //
///  ***     BROADCAST RECEIVER CLASS THAT RECEIVES THE COMPLETED WIFI SCANS AND       ********* //
///  *** ANALYZE THE RESULTS. IT HANDLES THE ONBUS DETECTION AND AUTOTICKET FUNCTIONS. ********* //
///  ******************************************************************************************* //


public class wifiReceiver extends BroadcastReceiver {


    public static boolean onBus = false;
    public static int[] lastResults = new int[10];
    public static String resultsToString = "";
    public static ArrayList<String> arrayList = null;
    static String TAG = "WIFIRECEIVER";
    List<ScanResult> results;
    int counter = 0;            // Counts the position where to write next result in the Array


    @Override

    ///*** WiFi scan results are received and processed through an algorithm to define
    ///*** if the user is on the bus.
    ///*** It reads a couple sensitivity options from SHARED PREFS

    public void onReceive(Context context, Intent intent) {


        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        boolean autoTicket = sharedPreferences.getBoolean("autoTicket", false);
        boolean autoOpen = sharedPreferences.getBoolean("autoOpen", true);

        int sensibility = valueOf(Objects.requireNonNull(sharedPreferences.getString("level", "5")));


        /// Triggered on wifi scan's result available broadcast received

        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        results = wifiManager.getScanResults();
        arrayList = new ArrayList<>();
        int isFound = 0;

        /// Counts the number of successful Wi-Fi scans.

        int successCounter = 0;     // Number of Success (2) in the last 10 attempts.

        /// We build a list with available networks.

        if (results.size() == 0) {
            Log.i(TAG, "EMPTY WIFI LIST");
        } else {
            Log.i(TAG, "RECEIVED WIFI LIST");


            /// Array created for development reasons
            for (ScanResult scanResult : results) {
                arrayList.add(scanResult.SSID + " - " + scanResult.BSSID + ", " + scanResult.level);
            }

            for (ScanResult scanResult : results) {

                /// TARGET MAC ADDRESSES ARE WRITTEN IN HERE!!

                /// COUPLE 1
                if (scanResult.BSSID.equals("00:3a:98:7d:4a:c1") && scanResult.level > -70) {
                    isFound++;
                }
                if (scanResult.BSSID.equals("00:3a:98:7d:4a:c2") && scanResult.level > -70) {
                    isFound++;
                }


                ///COUPLE 2
                if (scanResult.BSSID.equals("1c:b0:44:12:9f:de") && scanResult.level > -70) {
                    isFound++;
                }
                if (scanResult.BSSID.equals("62:b0:44:12:9f:df") && scanResult.level > -70) {
                    isFound++;
                }

                ///...
            }

            /// Eventually YOU ARE ON THE BUS IF YOU RECEIVE THE ACCESS POINTS WITH ENOUGH STRENGTH.
            /// Every Positive AP increments int isFound.
            /// If isFound == 2 (two strong APs in a single scan) enough times in the
            /// last 10 scans, YOU ARE CONSIDERED ON THE BUS.

            lastResults[counter] = isFound;
            counter++;

            resultsToString = "";
            for (int i = 0; i < 10; i++) {
                if (lastResults[i] == 2) {
                    successCounter++;
                }
            }

            Log.i(TAG, "SuccessCounter: " + successCounter);

            /// (sensibility) out of 10 attempts means onBus is validated.

            /// IN HERE GOES THE CODE TO HANDLE YOU ARE MOVING ON THE BUS
            if (successCounter >= sensibility && !onBus && !MainActivity.ticketvalid) {

                // The moment the user get on the bus && the ticket is not running

                onBus = true;

                //Toast.makeText(context, "YOU ARE ON THE BUS", Toast.LENGTH_SHORT).show();

                if (autoTicket) {

                    /// Case that defines the task if the autoTicket is turned on in settings

                    MainActivity.BuyTicket(context); // It simply buys the ticket

                } else if (autoOpen) {

                    /// Case that defines the task if the autoOpen is turned on in settings

                    /// It simply launches the main activity

                    PackageManager pm = context.getPackageManager();
                    String packageName = "com.example.busapp20";

                    Intent i = pm.getLaunchIntentForPackage(packageName);
                    if (i != null) {
                        context.startActivity(i);
                    } else {
                        Log.i(TAG, "Error opening the app automatically");
                    }
                } else if (!BackgroundService.notificationSent) {

                    // It knows if a notification has been sent already, elsewhere it shows

                    Intent i = new Intent(context, OnbusNotificationService.class);
                    context.startService(i);
                }


            } else if (successCounter < sensibility && onBus) {

                /// If the user gets off the bus
                onBus = false;
            }

            if (counter == 10) {           //Counter RESET to simulate queue.
                counter = 0;
            }
        }
    }
}

