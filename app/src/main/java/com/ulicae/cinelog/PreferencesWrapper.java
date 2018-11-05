package com.ulicae.cinelog;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferencesWrapper {

    public String getStringPreference(Context context, String key, String defaultValue) {
        return getPreferenceManager(context).getString(key, defaultValue);
    }

    private SharedPreferences getPreferenceManager(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
