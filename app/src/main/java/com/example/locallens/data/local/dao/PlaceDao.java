package com.example.locallens.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.locallens.data.local.entity.PlaceEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface PlaceDao {

    @Query("SELECT * FROM places")
    Flowable<List<PlaceEntity>> getAllPlaces();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertPlaces(List<PlaceEntity> places);

    @Query("DELETE FROM places")
    Completable deleteAllPlaces();
}
