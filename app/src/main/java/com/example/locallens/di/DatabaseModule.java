package com.example.locallens.di;

import android.content.Context;

import androidx.room.Room;

import com.example.locallens.data.local.dao.FavoriteDao;
import com.example.locallens.data.local.dao.IgnoredPlaceDao;
import com.example.locallens.data.local.dao.PlaceDao;
import com.example.locallens.data.local.database.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Provides
    @Singleton
    public AppDatabase provideAppDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                "locallens_database"
        ).build();
    }

    @Provides
    @Singleton
    public PlaceDao providePlaceDao(AppDatabase appDatabase) {
        return appDatabase.placeDao();
    }

    @Provides
    @Singleton
    public FavoriteDao provideFavoriteDao(AppDatabase appDatabase) {
        return appDatabase.favoriteDao();
    }

    @Provides
    @Singleton
    public IgnoredPlaceDao provideIgnoredPlaceDao(AppDatabase appDatabase) {
        return appDatabase.ignoredPlaceDao();
    }
}
