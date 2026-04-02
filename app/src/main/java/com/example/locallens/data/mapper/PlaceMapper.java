package com.example.locallens.data.mapper;

import com.example.locallens.data.local.entity.PlaceEntity;
import com.example.locallens.domain.model.Place;

/**
 * Mapper class for converting between Data layer entities and Domain layer models.
 */
public class PlaceMapper {

    private PlaceMapper() {
        // Prevent instantiation
    }

    public static Place toDomain(PlaceEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Place place = new Place(
                entity.getId(),
                entity.getName(),
                entity.getCategory(),
                entity.getAddress(),
                entity.getLatitude(),
                entity.getLongitude(),
                entity.getRating(),
                entity.getImageUrl(),
                "", // geohash - not stored in entity but will be mapped if needed
                0   // reviewCount - not stored in entity but will be mapped if needed
        );
        place.setIgnored(false); // Default to false, will be updated in Repository
        return place;
    }

    public static PlaceEntity toEntity(Place place) {
        if (place == null) {
            return null;
        }

        PlaceEntity entity = new PlaceEntity();
        entity.setId(place.getId());
        entity.setName(place.getName());
        entity.setCategory(place.getCategory());
        entity.setAddress(place.getAddress());
        entity.setLatitude(place.getLatitude());
        entity.setLongitude(place.getLongitude());
        entity.setRating(place.getRating());
        entity.setImageUrl(place.getImageUrl());
        
        return entity;
    }
}
