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

/// Not yet implemented in a working way. The list is temporarily fake. Its development is on the TO-DO list.
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

        /*

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back_chevron);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        */

        lvTravelHistory = findViewById(R.id.lvTravelHistory);


        String[] values = new String[]{"12/04/2019 12:21", "4/04/2019 15:04", "1/04/2019 11:29",
                "28/03/2019 19:51", "17/03/2019 9:18", "11/03/2019 22:10", "1/03/2019 15:20",
                "27/02/2019 1:16", "13/02/2019 17:11", "13/02/2019 12:46"};

        final ArrayList<String> list = new ArrayList<String>();

        list.add("12/04/2019 12:21");
        list.add("4/04/2019 15:04");
        list.add("1/04/2019 11:29");
        list.add("28/03/2019 19:51");
        list.add("17/03/2019 9:18");
        list.add("11/03/2019 22:10");
        list.add("1/03/2019 15:20");
        list.add("27/02/2019 1:16");
        list.add("13/02/2019 17:11");
        list.add("13/02/2019 12:46");


        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);

        lvTravelHistory.setAdapter(adapter);


        /*

        lvTravelHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                list.remove(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
            }

        });

        */
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

