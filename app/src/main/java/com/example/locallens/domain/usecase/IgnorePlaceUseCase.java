package com.example.locallens.domain.usecase;

import com.example.locallens.domain.repository.PlaceRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

/**
 * Pure Java Domain Use Case for ignoring a place.
 */
public class IgnorePlaceUseCase {

    private final PlaceRepository placeRepository;

    @Inject
    public IgnorePlaceUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public Completable execute(String placeId) {
        if (placeId == null || placeId.isEmpty()) {
            return Completable.error(new IllegalArgumentException("Place ID cannot be null or empty"));
        }
        return placeRepository.ignorePlace(placeId);
    }
}
