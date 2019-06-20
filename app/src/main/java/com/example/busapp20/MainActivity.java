package com.example.busapp20;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.example.busapp20.Background.BackgroundService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Locale;

import static com.example.busapp20.TopupActivity.MY_PREFS_NAME;
//  ******************************************************************************************* //
/// ***** BUSAPP IS AN APPLICATION DESIGNED TO ALLOW A TICKET PAYMENT ON A BUS OR SIMILAR       //
///        WITH A WIFI DETECTION SYSTEM. THE FINAL USER WILL BE ABLE TO PAY VIA PAYPAL.  ****** //
//  ******************************************************************************************* //


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final String TAG = "MAINACTIVITY";
    @SuppressLint("StaticFieldLeak")
    public static TextView time, time_label;
    public static boolean ticketvalid = false;
    TextView balanceAmount, username;
    Button btBuyTicket;

    ///  Buy a ticket with an Alert Dialog.
    // It doesn't require a View type argument. Used for Autoticket.
    public static void BuyTicket(@NonNull Context context) {
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
                MakeAlertDialog(context, "Not enough money for auto-ticket.\n" +
                        "Please top-up your account.");
            }
        }
    }

    /// Creates a Snackbar!
    private static void MakeSnackbar(View view, String string) {
        Snackbar.make(view, string, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    /// DEPRECATED: Creates an Alert Dialog with a single button.
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

    /// Method to show time TextView: when timer starts
    private static void Showtime() {
        time.setVisibility(View.VISIBLE);
        time_label.setVisibility(View.VISIBLE);
    }

    /// Method to hide time TextView: on app first start or when time finishes
    private static void HideTime() {

        Log.i("MAIN", "Now Timer should be set hidden!");
        time.setVisibility(View.INVISIBLE);
        time_label.setVisibility(View.INVISIBLE);
    }

    /// Rounding method
    public static double Rounding(double value, int numCifreDecimali) {
        double temp = Math.pow(10, numCifreDecimali);
        return Math.round(value * temp) / temp;
    }

    /// Method to set the ticket as bought
    private static void startTicket() {
        Showtime();
        ticketvalid = true;
        startTimer();
        BackgroundService.setNotificationDelay();
    }

    /// Method to set the ticket as not bought
    private static void stopTicket() {
        HideTime();
        ticketvalid = false;
        BackgroundService.notificationSent = false;

    }

    /// Method to start the ticket countdown timer
    private static void startTimer() {

        new CountDownTimer(5400000, 1000) {     //Ticket timer implementation
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000) % 60;              //millis formatting to human readable format.
                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
                time.setText(String.format(Locale.ITALY, "%02d : %02d : %02d", hours, minutes, seconds));
            }

            public void onFinish() {
                stopTicket();       // On time finish run function routine.
            }
        }.start();
    }

    /// Function to restart the app
    public static void restartApp(Context c) {
        try {
            //check if the context is given
            if (c != null) {
                //fetch the packagemanager so we can get the default launch activity
                // (you can replace this intent with any other activity if you want
                PackageManager pm = c.getPackageManager();
                //check if we got the PackageManager
                if (pm != null) {
                    //create the intent with the default start activity for your application
                    Intent mStartActivity = pm.getLaunchIntentForPackage(
                            c.getPackageName()
                    );
                    if (mStartActivity != null) {
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //create a pending intent so the application is restarted after System.exit(0) was called.
                        // We use an AlarmManager to call this intent in 100ms
                        int mPendingIntentId = 223344;
                        PendingIntent mPendingIntent = PendingIntent
                                .getActivity(c, mPendingIntentId, mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        //kill the application
                        System.exit(0);
                    } else {
                        Log.e(TAG, "Was not able to restart application, mStartActivity null");
                    }
                } else {
                    Log.e(TAG, "Was not able to restart application, PM null");
                }
            } else {
                Log.e(TAG, "Was not able to restart application, Context null");
            }
        } catch (Exception ex) {
            Log.e(TAG, "Was not able to restart application");
        }
    }

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

        // Connecting JAVA to XML
        time = findViewById(R.id.tvTimeLeft);
        time_label = findViewById(R.id.tvTimeLeftLabel);
        balanceAmount = findViewById(R.id.amountValue);
        username = findViewById(R.id.tvUsernameHome);
        btBuyTicket = findViewById(R.id.btBuyTicket);

        /// Open settings when click on username
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSettings();
            }
        });

        /// Action on ticket buy button pressed
        btBuyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyTicket(getBaseContext(), v);
            }
        });

        // Hides the timer if the ticket is not running
        if (!ticketvalid) {
            HideTime();
        }

        // Creates the Background Service that keeps the app active.
        startService(new Intent(this, BackgroundService.class));
    }

    // UI back button
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /// Menu UI Buttons -> On item pressed, a function to launch the correspondent activity is started.
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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

    /// EVERY TIME MAIN ACTIVITY IS FOCUSED OUR BALANCE IS UPDATED FROM SHARED PREFS
    @SuppressLint("SetTextI18n")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        refreshUsername();
        refreshBalance();
    }

    @Override
    public void onResume() {
        super.onResume();

        /// REQUIRES PERMISSIONS ON RUNTIME IF NEEDED!

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

        // END PERMISSION REQUIRES
    }

    ///  Buy a ticket and shows a snackbar notification in the current view.
    /// Used by the button in the MainActivity
    public void BuyTicket(@NonNull Context context, View view) {
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
                refreshBalance();
                MakeSnackbar(view, "Ticket bought correctly!");     //You succeeded in buying your ticket!
            } else {
                MakeSnackbar(view, "Not enough money. Please Top-up your account.");        // You failed because you are poor
            }
        } else {
            MakeSnackbar(view, "Ticket already bought.");           // You failed because tickevalid is true = you already have a valid ticket.
        }
    }

    /// Methods to launch secondary activities from side menu.
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

    /// Methods used to refresh the info on the Main Activity
    private void refreshBalance() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        float myBalance = prefs.getFloat("Balance", 0);
        balanceAmount.setText(String.format("%s €", Rounding(myBalance, 2)));
    }

    private void refreshUsername() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
        String name = sharedPreferences.getString("username", getString(R.string.go_to_settings));
        username.setText(name);
    }
}