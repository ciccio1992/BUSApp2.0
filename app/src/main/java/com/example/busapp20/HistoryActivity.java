package com.example.busapp20;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/// PROTOTYPE CLASS. Not yet implemented in a working way. The list is temporarily fake.

public class HistoryActivity extends AppCompatActivity {

    ListView lvTravelHistory = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        lvTravelHistory = findViewById(R.id.lvTravelHistory);

        final ArrayList<String> exampleList = new ArrayList<String>();

        exampleList.add("12/04/2019 12:21");
        exampleList.add("4/04/2019 15:04");
        exampleList.add("1/04/2019 11:29");
        exampleList.add("28/03/2019 19:51");
        exampleList.add("17/03/2019 9:18");
        exampleList.add("11/03/2019 22:10");
        exampleList.add("1/03/2019 15:20");
        exampleList.add("27/02/2019 1:16");
        exampleList.add("13/02/2019 17:11");
        exampleList.add("13/02/2019 12:46");


        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, exampleList);

        lvTravelHistory.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}

