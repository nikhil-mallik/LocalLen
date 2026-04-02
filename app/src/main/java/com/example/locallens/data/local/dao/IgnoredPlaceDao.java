package com.example.locallens.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.locallens.data.local.entity.IgnoredPlaceEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface IgnoredPlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertIgnoredPlace(IgnoredPlaceEntity place);

    @Query("SELECT id FROM ignored_places")
    Flowable<List<String>> getIgnoredPlaceIds();

    @Query("DELETE FROM ignored_places")
    Completable deleteAll();
}
