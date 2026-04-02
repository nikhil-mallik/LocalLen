package com.example.locallens.domain.usecase;

import com.example.locallens.domain.model.Place;
import com.example.locallens.domain.repository.PlaceRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Flowable;

/**
 * Use Case: Retrieve nearby places and filter ignored ones.
 */
public class GetNearbyPlacesUseCase {

    private final PlaceRepository placeRepository;

    @Inject
    public GetNearbyPlacesUseCase(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public Flowable<List<Place>> execute() {

        return placeRepository
                .getNearbyPlaces()

                .map(places -> {

                    List<Place> filteredPlaces = new ArrayList<>();

                    for (Place place : places) {

                        // Remove ignored places
                        if (!place.isIgnored()) {
                            filteredPlaces.add(place);
                        }
                    }

                    return filteredPlaces;
                });
    }
}