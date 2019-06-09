package com.example.busapp20;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.busapp20.MainActivity.wifiManager;
import static com.example.busapp20.wifiReceiver.lastResults;
import static com.example.busapp20.wifiReceiver.onBus;
import static com.example.busapp20.wifiReceiver.resultsToString;

public class ToolsActivity extends AppCompatActivity {

    Button btScan;
    TextView lastResultsRes, onbusres;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        btScan = findViewById(R.id.btScan);
        lastResultsRes = findViewById(R.id.tvLastResults);
        onbusres = findViewById(R.id.tvOnbus);

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

    void UpdateTestData () {
        resultsToString = "";
        for (int i = 0; i < 10; i++) {
            int temp = lastResults[i];
            String tempStr = String.valueOf(temp);
            resultsToString = resultsToString.concat(tempStr + " ");
        }

        onbusres.setText(String.valueOf(onBus));
        lastResultsRes.setText(resultsToString);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
