package com.example.busapp20;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import static com.example.busapp20.MainActivity.wifiManager;

public class ToolsActivity extends AppCompatActivity {
    Button btScan;
    TextView lastResultsRes, onbusres = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);

//        lastResultsRes = lastResultsRes.findViewById(R.id.tvLastResults);
  //      onbusres = onbusres.findViewById(R.id.tvOnbus);

        btScan = findViewById(R.id.btScan);

        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiManager.startScan();
            }
        });
    }



}
