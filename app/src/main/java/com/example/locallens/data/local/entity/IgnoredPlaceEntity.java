package com.example.locallens.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "ignored_places")
public class IgnoredPlaceEntity {

    @PrimaryKey
    @NonNull
    private String id;

    public IgnoredPlaceEntity() {
        this.id = "";
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }
}
