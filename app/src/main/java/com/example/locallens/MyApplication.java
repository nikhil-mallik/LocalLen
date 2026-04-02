package com.example.locallens;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

/**
 * Base Application class for Hilt Dependency Injection.
 */
@HiltAndroidApp
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize global configurations here if needed
    }
}
