package com.example.majun.rxjavademo;

import android.app.Application;

import com.example.majun.rxjavademo.utils.CrashHandler;

/**
 * Created by majun on 17/2/4.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }
}
