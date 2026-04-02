package com.example.locallens.domain.usecase;

import com.example.locallens.domain.repository.PlaceRepository;
import com.example.locallens.utils.LocationHelper;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Pure Java Domain Use Case for fetching nearby places based on the user's current location.
 */
public class FetchNearbyPlacesUseCase {

    private final PlaceRepository placeRepository;
    private final LocationHelper locationHelper;

    private static final double DEFAULT_RADIUS_KM = 10.0;

    @Inject
    public FetchNearbyPlacesUseCase(PlaceRepository placeRepository, LocationHelper locationHelper) {
        this.placeRepository = placeRepository;
        this.locationHelper = locationHelper;
    }

    public Completable execute() {
        return execute(DEFAULT_RADIUS_KM);
    }

    public Completable execute(double radiusKm) {
        return locationHelper.getLastLocation()
                .observeOn(Schedulers.io())
                .flatMap(location -> placeRepository.fetchNearbyPlaces(location.getLatitude(), location.getLongitude(), radiusKm))
                .flatMapCompletable(places -> placeRepository.savePlaces(places));
    }
}
