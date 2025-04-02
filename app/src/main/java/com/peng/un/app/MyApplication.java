package com.peng.un.app;

import android.app.Application;
import com.peng.un.data.Settings;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the Settings singleton
        Settings.instance(this);
    }
}
