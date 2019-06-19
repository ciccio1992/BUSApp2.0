package com.example.busapp20;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.busapp20.Background.BackgroundService.wifiManager;
import static com.example.busapp20.Background.wifiReceiver.lastResults;
import static com.example.busapp20.Background.wifiReceiver.onBus;
import static com.example.busapp20.Background.wifiReceiver.resultsToString;
import static com.example.busapp20.Background.wifiReceiver.arrayList;


//  ******************************************************************************************** //
/// *** THIS CLASS HANDLES THE TOOLS PAGE *** //
//  ******************************************************************************************** //


public class ToolsActivity extends AppCompatActivity {

    Button btScan;
    TextView tvLastResults, tvOnbus;
    ListView lvWifiScan;
    private static Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        btScan = findViewById(R.id.btScan);
        tvLastResults = findViewById(R.id.tvLastResults);
        tvOnbus = findViewById(R.id.tvOnbus);

        lvWifiScan = findViewById(R.id.lvWifiScan);

        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiManager.startScan();
                UpdateTestData();
            }
        });

        /*
        if (timer == null) {
            timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    resultsToString = "";
                    for (int i = 0; i < 10; i++) {
                        int temp = lastResults[i];
                        String tempStr = String.valueOf(temp);
                        resultsToString = resultsToString.concat(tempStr + " ");
                    }

                    tvOnbus.setText(String.valueOf(onBus));
                    tvLastResults.setText(resultsToString);

                    if (!(arrayList.isEmpty())) {

                        lvWifiScan.setAdapter(new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_2, arrayList));
                    }
                }
            };
            timer.scheduleAtFixedRate(timerTask, 0, 2000);
        }
        */
    }

    ///*** Updates data received from wifiReceiver.java
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    ///*** Uses values from wifiReceiver to build a String and show it in the Textview
    ///*** and shows the wifi list in the Listview
    private void UpdateTestData() {
        resultsToString = "";
        for (int i = 0; i < 10; i++) {
            int temp = lastResults[i];
            String tempStr = String.valueOf(temp);
            resultsToString = resultsToString.concat(tempStr + " ");
        }

        tvOnbus.setText(String.valueOf(onBus));
        tvLastResults.setText(resultsToString);

        if (!(arrayList.isEmpty())) {

            lvWifiScan.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_2, arrayList));


        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
