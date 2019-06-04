package com.example.busapp20;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    // Button btEnableWifi, btDisableWifi, btGetData, btScan, settingsButton;
    static WifiManager wifiManager;

    BroadcastReceiver myReceiver = null;
    public int validatorCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Not available yet.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        // CONNECTING UI ITEMS TO JAVA
        /*
        settingsButton = findViewById(R.id.settingsButton);
        btEnableWifi = findViewById(R.id.btEnableWifi);
        btDisableWifi = findViewById(R.id.btDisableWifi);
        btGetData = findViewById(R.id.btGetStatus);
        wifiReceiver.tvStatus = findViewById(R.id.tvStatus); */


        startService(new Intent(this, BackgroundService.class));

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        myReceiver = new wifiReceiver();
        registerReceiver(myReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        // Buttons to Enable\Disable WIFI


        /*
        btEnableWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiManager.setWifiEnabled(true);
                Toast.makeText(MainActivity.this, "WiFi Enabled", Toast.LENGTH_SHORT).show();
                WifiLoopTimer.start();
            }
        });

        btDisableWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiManager.setWifiEnabled(false);
                Toast.makeText(MainActivity.this, "WiFi Disabled", Toast.LENGTH_SHORT).show();
            }
        });
*/


        // This section of code handles the WiFi network search and returns a ListArray with all Networks.

        //  listView = findViewById(R.id.wifiList);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        } else if (id == R.id.nav_send) {
             openSend();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {           //THIS CODE REQUIRES PERMISSIONS ON RUNTIME!
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
        Intent intent = new Intent(this, SendActivity.class);
        startActivity(intent);
    }

    public void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}

/*
if
 */