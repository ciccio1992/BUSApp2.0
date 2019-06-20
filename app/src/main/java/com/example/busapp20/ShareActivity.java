package com.example.busapp20;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

//  ******************************************************************************************** //
///        THS CLASS HANDLES THE SHARE PAGE. IT IS ONLY SHOWN AS A FUTURE IMPLEMENTATION.        //
//  ******************************************************************************************** //

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
