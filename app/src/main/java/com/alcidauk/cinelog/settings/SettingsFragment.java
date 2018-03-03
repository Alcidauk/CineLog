package com.alcidauk.cinelog.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.alcidauk.cinelog.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.app_preferences);
    }

}

