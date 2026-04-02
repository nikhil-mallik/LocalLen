package com.example.locallens.domain.repository;

import com.example.locallens.domain.model.Place;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

/**
 * Pure Java domain interface for Place operations.
 * Belongs strictly to the Domain Layer.
 */
public interface PlaceRepository {

    Flowable<List<Place>> getNearbyPlaces();

    Single<List<Place>> fetchNearbyPlaces(double latitude, double longitude, double radiusKm);

    Completable savePlaces(List<Place> places);

    Completable addToFavorites(Place place);

    Completable ignorePlace(String placeId);

    Completable clearIgnoredPlaces();

    Flowable<List<Place>> getFavoritePlaces();
}
