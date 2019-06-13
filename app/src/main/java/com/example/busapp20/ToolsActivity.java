package com.example.busapp20;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.busapp20.Background.BackgroundService.wifiManager;
import static com.example.busapp20.Background.wifiReceiver.lastResults;
import static com.example.busapp20.Background.wifiReceiver.onBus;
import static com.example.busapp20.Background.wifiReceiver.resultsToString;
import static com.example.busapp20.Background.wifiReceiver.arrayList;


public class ToolsActivity extends AppCompatActivity {

    Button btScan;
    TextView tvLastResults, tvOnbus;
    ListView lvWifiScan;

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
    }

    ///UPDATING DATA RECEIVED FROM wifiReceiver.java

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        UpdateTestData();
    }

    void UpdateTestData() {
        resultsToString = "";
        for (int i = 0; i < 10; i++) {
            int temp = lastResults[i];
            String tempStr = String.valueOf(temp);
            resultsToString = resultsToString.concat(tempStr + " ");
        }

        tvOnbus.setText(String.valueOf(onBus));
        tvLastResults.setText(resultsToString);

        if (!(arrayList.isEmpty())) {

            lvWifiScan.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList));


        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
