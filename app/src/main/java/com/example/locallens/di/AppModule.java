package com.example.locallens.di;

import android.app.Application;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    public Context provideApplicationContext(Application application) {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    public FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    public com.google.android.gms.location.FusedLocationProviderClient provideFusedLocationProviderClient(@dagger.hilt.android.qualifiers.ApplicationContext Context context) {
        return com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(context);
    }

    @Provides
    @Singleton
    public com.google.firebase.firestore.FirebaseFirestore provideFirebaseFirestore() {
        return com.google.firebase.firestore.FirebaseFirestore.getInstance();
    }
}
