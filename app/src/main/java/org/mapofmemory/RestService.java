package org.mapofmemory;

import org.mapofmemory.entities.PlaceEntity;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

/**
 * Created by The Tronuo on 23.01.2018.
 */

public interface RestService{
    @GET("places.php")
    Single<List<PlaceEntity>> getPlaces();
}
