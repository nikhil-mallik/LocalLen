package com.example.locallens.data.local.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.locallens.data.local.dao.FavoriteDao;
import com.example.locallens.data.local.dao.IgnoredPlaceDao;
import com.example.locallens.data.local.dao.PlaceDao;
import com.example.locallens.data.local.entity.FavoriteEntity;
import com.example.locallens.data.local.entity.IgnoredPlaceEntity;
import com.example.locallens.data.local.entity.PlaceEntity;

@Database(
    entities = {
        PlaceEntity.class,
        FavoriteEntity.class,
        IgnoredPlaceEntity.class
    },
    version = 1,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract PlaceDao placeDao();
    
    public abstract FavoriteDao favoriteDao();
    
    public abstract IgnoredPlaceDao ignoredPlaceDao();
}
