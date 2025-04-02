package com.peng.un.app;

import android.app.Application;
import android.util.Log;
import com.peng.un.data.Settings;
import com.peng.un.utils.ALog;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 打印日志
        ALog.d("MyApplication onCreate");
        // Initialize the Settings singleton
        Settings.instance(this);
    }
}
