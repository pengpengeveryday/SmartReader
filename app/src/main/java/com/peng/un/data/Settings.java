package com.peng.un.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

public class Settings {
    private static final String PREFS_NAME = "SmartReaderPrefs";
    private final SharedPreferences preferences;
    public final Context context;
    private static Settings instance;

    private Settings(Context context) {
        // Use the application context to avoid memory leaks
        this.context = context.getApplicationContext();
        preferences = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static Settings instance(Context context) {
        if (instance == null) {
            instance = new Settings(context);
        }
        return instance;
    }

    public static Settings instance() {
        if (instance == null) {
            throw new IllegalStateException("Settings instance has not been initialized. Call instance(Context) first.");
        }
        return instance;
    }

    public void saveLastPath(String path) {
        preferences.edit().putString("last_path", path).apply();
    }

    public String getLastPath() {
        // Use Environment.getExternalStorageDirectory().getPath() instead of hard - coded "/sdcard"
        return preferences.getString("last_path", Environment.getExternalStorageDirectory().getPath());
    }
}
