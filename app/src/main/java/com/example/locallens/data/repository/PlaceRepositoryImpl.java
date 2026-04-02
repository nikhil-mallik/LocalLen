package com.example.locallens.data.repository;

import com.example.locallens.data.local.dao.FavoriteDao;
import com.example.locallens.data.local.dao.IgnoredPlaceDao;
import com.example.locallens.data.local.dao.PlaceDao;
import com.example.locallens.data.local.entity.FavoriteEntity;
import com.example.locallens.data.local.entity.IgnoredPlaceEntity;
import com.example.locallens.data.local.entity.PlaceEntity;
import com.example.locallens.data.mapper.PlaceMapper;
import com.example.locallens.data.remote.datasource.PlacesRemoteDataSource;
import com.example.locallens.data.remote.dto.PlaceDto;
import com.example.locallens.domain.model.Place;
import com.example.locallens.domain.repository.PlaceRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

public class PlaceRepositoryImpl implements PlaceRepository {

    private final PlaceDao placeDao;
    private final FavoriteDao favoriteDao;
    private final IgnoredPlaceDao ignoredPlaceDao;
    private final PlacesRemoteDataSource placesRemoteDataSource;

    @Inject
    public PlaceRepositoryImpl(PlaceDao placeDao, FavoriteDao favoriteDao, IgnoredPlaceDao ignoredPlaceDao, PlacesRemoteDataSource placesRemoteDataSource) {
        this.placeDao = placeDao;
        this.favoriteDao = favoriteDao;
        this.ignoredPlaceDao = ignoredPlaceDao;
        this.placesRemoteDataSource = placesRemoteDataSource;
    }

    @Override
    public Flowable<List<Place>> getNearbyPlaces() {
        return Flowable.combineLatest(
                placeDao.getAllPlaces(),
                ignoredPlaceDao.getIgnoredPlaceIds(),
                (entities, ignoredIds) -> {
                    List<Place> domainPlaces = new ArrayList<>();
                    for (PlaceEntity entity : entities) {
                        Place domainPlace = PlaceMapper.toDomain(entity);
                        if (ignoredIds.contains(entity.getId())) {
                            domainPlace.setIgnored(true);
                        }
                        domainPlaces.add(domainPlace);
                    }
                    return domainPlaces;
                }
        );
    }

    @Override
    public Single<List<Place>> fetchNearbyPlaces(double latitude, double longitude, double radiusKm) {
        return placesRemoteDataSource.getNearbyPlaces(latitude, longitude, radiusKm)
                .map(placeDtos -> {
                    List<Place> places = new ArrayList<>();
                    for (PlaceDto dto : placeDtos) {
                        Place place = new Place(
                                dto.getId(),
                                dto.getName(),
                                dto.getCategory(),
                                dto.getAddress(),
                                dto.getLatitude(),
                                dto.getLongitude(),
                                dto.getRating(),
                                dto.getImageUrl(),
                                dto.getGeohash(),
                                dto.getReviewCount()
                        );
                        places.add(place);
                    }
                    return places;
                });
    }

    @Override
    public Completable savePlaces(List<Place> places) {
        List<PlaceEntity> entities = new ArrayList<>();
        for (Place place : places) {
            entities.add(PlaceMapper.toEntity(place));
        }
        return placeDao.insertPlaces(entities);
    }

    @Override
    public Completable addToFavorites(Place place) {
        FavoriteEntity entity = new FavoriteEntity();
        if (place.getId() != null) {
            entity.setId(place.getId());
        }
        entity.setName(place.getName());
        entity.setCategory(place.getCategory());
        entity.setAddress(place.getAddress());
        entity.setLatitude(place.getLatitude());
        entity.setLongitude(place.getLongitude());
        entity.setRating(place.getRating());
        entity.setImageUrl(place.getImageUrl());

        return favoriteDao.insertFavorite(entity);
    }

    @Override
    public Completable ignorePlace(String placeId) {
        IgnoredPlaceEntity entity = new IgnoredPlaceEntity();
        if (placeId != null) {
            entity.setId(placeId);
        }
        return ignoredPlaceDao.insertIgnoredPlace(entity);
    }

    @Override
    public Completable clearIgnoredPlaces() {
        return ignoredPlaceDao.deleteAll();
    }

    @Override
    public Flowable<List<Place>> getFavoritePlaces() {
        return favoriteDao.getAllFavorites()
                .map(entities -> {
                    List<Place> places = new ArrayList<>();
                    for (FavoriteEntity entity : entities) {
                        places.add(new Place(
                                entity.getId(),
                                entity.getName(),
                                entity.getCategory(),
                                entity.getAddress(),
                                entity.getLatitude(),
                                entity.getLongitude(),
                                entity.getRating(),
                                entity.getImageUrl(),
                                "", // geohash not in favorites
                                0   // reviewCount not in favorites
                        ));
                    }
                    return places;
                });
    }
}
