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
    SwitchPreference switchPreferenceSortArtist;
    SwitchPreference switchPreferenceSortRankingName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        switchPreferenceSortArtist = (SwitchPreference) findPreference("sortArtist");
        switchPreferenceSortRankingName = (SwitchPreference) findPreference("sortRankingName");
        switchPreferenceSortArtist.setOnPreferenceChangeListener(
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
        switchPreferenceSortRankingName.setOnPreferenceChangeListener(
                new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object newValue) {
                        String toastMsg = "";
                        if ((Boolean) newValue)
                            toastMsg = getString(R.string.preferences_sort_ranking_name_toast_true);
                        else
                            toastMsg = getString(R.string.preferences_sort_ranking_name_toast_false);
                        Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
                        return true;
                    }
                }
        );
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
