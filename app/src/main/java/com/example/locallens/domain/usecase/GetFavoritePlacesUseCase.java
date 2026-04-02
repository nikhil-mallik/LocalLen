package com.example.locallens.domain.usecase;

import com.example.locallens.domain.model.Place;
import com.example.locallens.domain.repository.PlaceRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

/**
 * Pure Java Domain Use Case for retrieving favorite places.
 */
public class GetFavoritePlacesUseCase {

    private final PlaceRepository placeRepository;

    @Inject
    public GetFavoritePlacesUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public Flowable<List<Place>> execute() {
        return placeRepository.getFavoritePlaces();
    }
}
