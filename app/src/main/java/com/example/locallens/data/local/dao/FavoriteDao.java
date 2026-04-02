package com.example.locallens.data.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.locallens.data.local.entity.FavoriteEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertFavorite(FavoriteEntity favorite);

    @Query("SELECT * FROM favorites")
    Flowable<List<FavoriteEntity>> getAllFavorites();

    @Query("DELETE FROM favorites WHERE id = :placeId")
    Completable removeFavorite(String placeId);
}
