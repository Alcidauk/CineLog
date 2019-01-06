package com.ulicae.cinelog.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * CineLog Copyright 2018 Pierre Rognon
 *
 *
 * This file is part of CineLog.
 * CineLog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CineLog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CineLog. If not, see <https://www.gnu.org/licenses/>.
 *
 */
public class PreferencesWrapper {

    public String getStringPreference(Context context, String key, String defaultValue) {
        return getPreferenceManager(context).getString(key, defaultValue);
    }

    public boolean getBooleanPreference(Context context, String key, boolean defaultValue) {
        return getPreferenceManager(context).getBoolean(key, defaultValue);
    }

    public int getIntegerPreference(Context context, String key, int defaultValue) {
        return getPreferenceManager(context).getInt(key, defaultValue);
    }

    public void setStringPreference(Context context, String key, String defaultValue) {
        getPreferenceManager(context).edit().putString(key, defaultValue).apply();
    }

    public void setIntegerPreference(Context context, String key, int defaultValue) {
        getPreferenceManager(context).edit().putInt(key, defaultValue).apply();
    }

    private SharedPreferences getPreferenceManager(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
