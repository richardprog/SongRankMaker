package com.example.exoest.songrankmaker.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.example.exoest.songrankmaker.R;

/**
 * Created by exoest on 19/5/2017.
 */

public class PreferencesFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    View view;
    SwitchPreference switchPreference;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        switchPreference = (SwitchPreference) findPreference("sortArtist");
        switchPreference.setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        String toastMsg = "";
                        if ((Boolean) newValue)
                            toastMsg = getString(R.string.preferences_sort_artist_toast_true);
                        else
                            toastMsg = getString(R.string.preferences_sort_artist_toast_false);
                        Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
                        return true;
                    }
                }
        );
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("sortArtist")){
            if (sharedPreferences.getBoolean("sortArtist", false)) {
                Toast.makeText(view.getContext(), "Song artist sorted.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
