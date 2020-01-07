package com.example.taskroom;

import android.app.Application;
import android.util.Log;

public class TaskApplication extends Application {

    private static final String LOG_TAG = TaskApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d( LOG_TAG, "onCreate");
    }
}
