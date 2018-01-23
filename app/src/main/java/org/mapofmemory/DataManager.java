package org.mapofmemory;

import org.mapofmemory.entities.PlaceEntity;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by The Tronuo on 23.01.2018.
 */

public class DataManager {
    private Retrofit retrofit;
    private RestService restService;

    public DataManager(){
        init();
    }


    private void init(){
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        restService = retrofit.create(RestService.class);
    }

    public Single<List<PlaceEntity>> getPlaces(){
        return restService.getPlaces();
    }
}
