package com.peng.un.data;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
    private static final String PREFS_NAME = "SmartReaderPrefs";
    private final SharedPreferences preferences;
    private static Settings instance;

    private Settings(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static Settings instance(Context context) {
        if (instance == null) {
            instance = new Settings(context.getApplicationContext());
        }
        return instance;
    }

    public void saveLastPath(String path) {
        preferences.edit().putString("last_path", path).apply();
    }

    public String getLastPath() {
        return preferences.getString("last_path", null);
    }
} 