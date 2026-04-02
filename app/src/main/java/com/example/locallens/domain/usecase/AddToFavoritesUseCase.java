package com.example.locallens.domain.usecase;

import com.example.locallens.domain.model.Place;
import com.example.locallens.domain.repository.PlaceRepository;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;

/**
 * Pure Java Domain Use Case for adding a place to favorites.
 */
public class AddToFavoritesUseCase {

    private final PlaceRepository placeRepository;

    @Inject
    public AddToFavoritesUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public Completable execute(Place place) {
        if (place == null || place.getId() == null) {
            return Completable.error(new IllegalArgumentException("Place or Place ID cannot be null"));
        }
        return placeRepository.addToFavorites(place);
    }
}
