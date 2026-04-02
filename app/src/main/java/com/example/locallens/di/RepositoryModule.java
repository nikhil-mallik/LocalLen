package com.example.locallens.di;

import com.example.locallens.data.repository.PlaceRepositoryImpl;
import com.example.locallens.domain.repository.PlaceRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class RepositoryModule {

    @Binds
    @Singleton
    public abstract PlaceRepository bindPlaceRepository(PlaceRepositoryImpl impl);
}
