package com.example.busapp20;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

//  ******************************************************************************************** //
/// ***                 THIS CLASS HANDLES THE SETTINGS "SHARED PREFS" PAGE                  *** //
//  ******************************************************************************************** //

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            /// This sets the alert that shows when you change the scan frequency setting
            final ListPreference frequency = findPreference("frequency");
            Preference restart = findPreference("restart");

            frequency.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String string = "You need to restart the app for this setting to be applied.";
                    AlertDialog.Builder AlertBuilder = new AlertDialog.Builder(getContext())
                            .setTitle("Warning")
                            .setMessage(string)
                            .setCancelable(true)
                            .setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = AlertBuilder.create();
                    alertDialog.show();
                    try {
                        frequency.setValue(newValue.toString());
                    } catch (Exception e) {
                        // Exception handling
                        e.printStackTrace();
                        // We print StackTrace in case of errors
                    }
                    return false;
                }
            });

            restart.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    MainActivity.restartApp(getContext());
                    return false;
                }
            });

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }


}