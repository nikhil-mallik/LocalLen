package com.example.locallens.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;
import io.reactivex.rxjava3.core.Single;

@Singleton
public class LocationHelper {

    private final FusedLocationProviderClient fusedLocationProviderClient;
    private final Context context;

    @Inject
    public LocationHelper(
            FusedLocationProviderClient fusedLocationProviderClient,
            @ApplicationContext Context context) {
        this.fusedLocationProviderClient = fusedLocationProviderClient;
        this.context = context;
    }

    public Single<Location> getLastLocation() {
        return Single.create(emitter -> {
            boolean hasFineLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
            boolean hasCoarseLocation = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;

            if (!hasFineLocation && !hasCoarseLocation) {
                if (!emitter.isDisposed()) {
                    emitter.onError(new SecurityException("Location permissions are not granted"));
                }
                return;
            }

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            if (!emitter.isDisposed()) {
                                emitter.onSuccess(location);
                            }
                        } else {
                            if (!emitter.isDisposed()) {
                                emitter.onError(new Exception("Last known location is null"));
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        if (!emitter.isDisposed()) {
                            emitter.onError(e);
                        }
                    });
        });
    }
}
