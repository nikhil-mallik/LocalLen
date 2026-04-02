package com.example.locallens.data.remote.datasource;

import com.example.locallens.data.remote.dto.PlaceDto;
import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

public class PlacesRemoteDataSource {

    private final FirebaseFirestore db;

    @Inject
    public PlacesRemoteDataSource(FirebaseFirestore db) {
        this.db = db;
    }

    /**
     * Fetches nearby places from Firestore using geohashing.
     *
     * @param latitude  Center latitude
     * @param longitude Center longitude
     * @param radiusKm  Search radius in kilometers
     * @return Single emitting a list of nearby places
     */
    public Single<List<PlaceDto>> getNearbyPlaces(double latitude, double longitude, double radiusKm) {
        return Single.create(emitter -> {
            GeoLocation center = new GeoLocation(latitude, longitude);
            double radiusInM = radiusKm * 1000;

            // Each item in 'bounds' represents a startAt/endAt pair. 
            // We have to issue a separate query for each pair.
            List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM);
            List<Task<QuerySnapshot>> tasks = new ArrayList<>();

            for (GeoQueryBounds b : bounds) {
                Query q = db.collection("places")
                        .orderBy("geohash")
                        .startAt(b.startHash)
                        .endAt(b.endHash);

                tasks.add(q.get());
            }

            Tasks.whenAllComplete(tasks).addOnCompleteListener(t -> {
                List<PlaceDto> matchingDocs = new ArrayList<>();

                for (Task<QuerySnapshot> task : tasks) {
                    if (task.isSuccessful()) {
                        QuerySnapshot snap = task.getResult();
                        for (DocumentSnapshot doc : snap.getDocuments()) {
                            double lat = doc.getDouble("latitude");
                            double lng = doc.getDouble("longitude");

                            // We need to filter out few false positives due to GeoHash 
                            // accuracy limitations at edge cases.
                            GeoLocation docLocation = new GeoLocation(lat, lng);
                            double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center);
                            if (distanceInM <= radiusInM) {
                                PlaceDto dto = doc.toObject(PlaceDto.class);
                                if (dto != null) {
                                    dto.setId(doc.getId());
                                    matchingDocs.add(dto);
                                }
                            }
                        }
                    } else if (task.getException() != null) {
                        emitter.onError(task.getException());
                        return;
                    }
                }
                emitter.onSuccess(matchingDocs);
            });
        });
    }
}
