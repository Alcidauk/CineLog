package com.ulicae.cinelog.utils;

import android.content.Context;

import com.ulicae.cinelog.R;

public class ThemeWrapper {

    public void setThemeWithPreferences(Context context) {
        if (new PreferencesWrapper().getBooleanPreference(context.getApplicationContext(), "theme", false)) {
            context.setTheme(R.style.AppThemeDark);
        } else {
            context.setTheme(R.style.AppTheme);
        }
    }
}
