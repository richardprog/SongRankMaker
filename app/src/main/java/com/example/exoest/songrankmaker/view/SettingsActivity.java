package com.example.exoest.songrankmaker.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferencesFragment pf = new PreferencesFragment();
        getFragmentManager().beginTransaction().replace(android.R.id.content, pf).commit();
    }


}
