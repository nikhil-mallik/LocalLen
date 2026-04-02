package com.example.locallens.utils;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class PreferenceManager {

    private static final String PREF_NAME = "local_lens_prefs";
    private static final String KEY_DARK_MODE = "dark_mode_enabled";

    private final SharedPreferences sharedPreferences;

    @Inject
    public PreferenceManager(@ApplicationContext Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setDarkModeEnabled(boolean enabled) {
        sharedPreferences.edit().putBoolean(KEY_DARK_MODE, enabled).apply();
    }

    public boolean isDarkModeEnabled() {
        return sharedPreferences.getBoolean(KEY_DARK_MODE, false); // Default is false
    }

    public void clearIgnoredPlaces() {
        // Implementation for clearing ignored places preference if it was stored here
        // Note: Actual DB clearing should happen via IgnoredPlaceDao/Repository
    }
}
