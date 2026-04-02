package com.example.locallens.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Helper class to handle Android runtime permissions for location services.
 */
public class PermissionHelper {

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    /**
     * Checks if the app has been granted the ACCESS_FINE_LOCATION permission.
     *
     * @param context The application or activity context.
     * @return true if permission is granted, false otherwise.
     */
    public static boolean hasLocationPermission(Context context) {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Requests the ACCESS_FINE_LOCATION permission from the user.
     *
     * @param activity The activity from which the request is initiated.
     */
    public static void requestLocationPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }
}
