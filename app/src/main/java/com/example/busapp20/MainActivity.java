package com.example.busapp20;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import com.example.busapp20.Background.BackgroundService;
import com.example.busapp20.Background.wifiReceiver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.text.format.DateUtils;
import android.text.style.TtsSpan;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Date;

import static com.example.busapp20.TopupActivity.MY_PREFS_NAME;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    public static WifiManager wifiManager;
    TextView balanceAmount, username;
    public static TextView time, time_label;
    Button btBuyTicket;

    public static boolean ticketvalid = false;

    BroadcastReceiver myReceiver = null;
    // public int validatorCounter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbarHistory);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MakeSnackbar(view, "Not available yet.");
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        time = findViewById(R.id.tvTimeLeft);
        time_label = findViewById(R.id.tvTimeLeftLabel);

        HideTime();

        balanceAmount = findViewById(R.id.amountValue);

        username = findViewById(R.id.tvUsernameHome);

        btBuyTicket = findViewById(R.id.btBuyTicket);

        btBuyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyTicketSnackbarVersion(getBaseContext(), v);
            }
        });


        startService(new Intent(this, BackgroundService.class));

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        myReceiver = new wifiReceiver();
        registerReceiver(myReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_history) {
            openHistory();
        } else if (id == R.id.nav_tools) {
            openTools();
        } else if (id == R.id.nav_share) {
            openShare();
        } else if (id == R.id.nav_topup) {
            openSend();
        } else if (id == R.id.nav_settings) {
            openSettings();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();

        ///THE FOLLOWING CODE REQUIRES PERMISSIONS ON RUNTIME!

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 87);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 87);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.INTERNET}, 87);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 87);
            }
        }

        /// END PERMISSION REQUIRES
    }

    /// EVERY TIME MAIN ACTIVITY IS FOCUSED OUR BALANCE IS UPDATED FROM SHAREDPREFS
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String name = sharedPreferences.getString("username", getString(R.string.go_to_settings));

        username.setText(name);


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        float myBalance = prefs.getFloat("Balance", 0);
        balanceAmount.setText(myBalance + " €");
    }


    public void openHistory() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    public void openTools() {
        Intent intent = new Intent(this, ToolsActivity.class);
        startActivity(intent);
    }

    public void openShare() {
        Intent intent = new Intent(this, ShareActivity.class);
        startActivity(intent);
    }

    public void openSend() {
        Intent intent = new Intent(this, TopupActivity.class);
        startActivity(intent);
    }

    public void openSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


    ///***  NEW Function. Same as following but uses snackbar instead of old popup.
    public void BuyTicketSnackbarVersion(@NonNull Context context, View view) {

        if (!ticketvalid) {     //We check whether if the user already nb
            SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            float myPrevBalance = prefs.getFloat("Balance", 0);     // Retrieving our current balance from SharedPrefs.

            /// Updating Balance on SharedPreferences
            float newBalance = (myPrevBalance - 1.5f);      //Price of ticket taken from balance.
            if (newBalance >= 0) {                          //Positive balance check, if balance becomes negative you are unable to buy a ticket.
                SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putFloat("Balance", newBalance);
                editor.apply();
                // New Data Applied
                startTicket();
                MakeSnackbar(view, "Ticket bought correctly!");     //You succeeded in buying your ticket!
            } else {
                MakeSnackbar(view, "Not enough money. Please Top-up your account.");        // You failed because you are poor
            }
        } else {
            MakeSnackbar(view, "Ticket already bought.");           // You failed because tickevalid is true = you already have a valid ticket.
        }
    }


    ///***  DEPRECATED Function. Used to show a popup instead of a snackbar.
    public static void BuyTicketAlertDialogVersion(@NonNull Context context) {
        if (!ticketvalid) {
            /// Code to get our previous balance from SharedPreferences
            SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            float myPrevBalance = prefs.getFloat("Balance", 0);

            // Updating Balance on SharedPreferences
            float newBalance = (myPrevBalance - 1.5f);
            if (newBalance >= 0) {
                SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putFloat("Balance", newBalance);
                editor.apply();
                // New Data Applied
                startTicket();
            } else {
                MakeAlertDialog(context, "Not enough money for auto-ticket.\nPlease top-up your account.");
            }
        }
    }


    private static void startTicket() {
        Showtime();
        ticketvalid = true;
    }

    private void stopTicket() {
        HideTime();
        ticketvalid = false;
    }

    private static void MakeSnackbar(View view, String string) {
        Snackbar.make(view, string, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public static void MakeAlertDialog(Context context, String string) {
        AlertDialog.Builder AlertBuilder = new AlertDialog.Builder(context);
        AlertBuilder.setMessage(string);
        AlertBuilder.setCancelable(true);
        AlertBuilder.setPositiveButton(
                "Ok!",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = AlertBuilder.create();
        alertDialog.show();
    }

    private static void Showtime() {
        time.setVisibility(View.VISIBLE);
        time_label.setVisibility(View.VISIBLE);
    }

    private static void HideTime() {
        time.setVisibility(View.INVISIBLE);
        time_label.setVisibility(View.INVISIBLE);
    }

}

/*
if
 */