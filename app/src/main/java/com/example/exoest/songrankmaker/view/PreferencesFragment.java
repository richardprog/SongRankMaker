package com.example.exoest.songrankmaker.view;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import com.example.exoest.songrankmaker.R;

/**
 * Created by exoest on 19/5/2017.
 */

public class PreferencesFragment extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
