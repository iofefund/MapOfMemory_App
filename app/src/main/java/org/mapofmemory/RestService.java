package org.mapofmemory;

import org.mapofmemory.entities.AboutEntity;
import org.mapofmemory.entities.DayOfMemory;
import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.entities.PlaceEntity;
import org.mapofmemory.entities.RouteEntity;

import java.util.List;

import io.reactivex.Single;
import okhttp3.Route;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by The Tronuo on 23.01.2018.
 */

public interface RestService{
    @GET("places.php")
    Single<List<PlaceEntity>> getPlaces();

    @GET("monuments.php")
    Single<List<MonumentEntity>> getMonuments();

    @GET("about.php")
    Single<AboutEntity> getAboutInfo(@Query("place_id") int placeId);

    @GET("route.php")
    Single<RouteEntity> getRouteInfo(@Query("place_id") int placeId);

    @GET("day_of_memories.php")
    Single<List<DayOfMemory>> getDOM(@Query("place_id") int placeId);
}
