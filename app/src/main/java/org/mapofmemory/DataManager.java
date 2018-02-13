package org.mapofmemory;

import android.content.Context;

import com.pushtorefresh.storio3.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio3.sqlite.StorIOSQLite;
import com.pushtorefresh.storio3.sqlite.impl.DefaultStorIOSQLite;

import org.mapofmemory.entities.AboutEntity;
import org.mapofmemory.entities.AboutEntityStorIOSQLiteDeleteResolver;
import org.mapofmemory.entities.AboutEntityStorIOSQLiteGetResolver;
import org.mapofmemory.entities.AboutEntityStorIOSQLitePutResolver;
import org.mapofmemory.entities.DayOfMemory;
import org.mapofmemory.entities.DayOfMemoryStorIOSQLiteDeleteResolver;
import org.mapofmemory.entities.DayOfMemoryStorIOSQLiteGetResolver;
import org.mapofmemory.entities.DayOfMemoryStorIOSQLitePutResolver;
import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.entities.MonumentEntityStorIOSQLiteDeleteResolver;
import org.mapofmemory.entities.MonumentEntityStorIOSQLiteGetResolver;
import org.mapofmemory.entities.MonumentEntityStorIOSQLitePutResolver;
import org.mapofmemory.entities.MonumentImgEntity;
import org.mapofmemory.entities.MonumentResponse;
import org.mapofmemory.entities.PersonEntity;
import org.mapofmemory.entities.PersonEntityStorIOSQLiteDeleteResolver;
import org.mapofmemory.entities.PersonEntityStorIOSQLiteGetResolver;
import org.mapofmemory.entities.PersonEntityStorIOSQLitePutResolver;
import org.mapofmemory.entities.PlaceEntity;
import org.mapofmemory.entities.PlaceEntityStorIOSQLiteDeleteResolver;
import org.mapofmemory.entities.PlaceEntityStorIOSQLiteGetResolver;
import org.mapofmemory.entities.PlaceEntityStorIOSQLitePutResolver;
import org.mapofmemory.entities.RouteEntity;
import org.mapofmemory.entities.RouteEntityStorIOSQLiteDeleteResolver;
import org.mapofmemory.entities.RouteEntityStorIOSQLiteGetResolver;
import org.mapofmemory.entities.RouteEntityStorIOSQLitePutResolver;

import java.util.List;

import io.reactivex.Single;
import okhttp3.Route;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by The Tronuo on 23.01.2018.
 */

public class DataManager {
    private Retrofit retrofit;
    public SharedPrefs sharedPrefs;
    private RestService restService;
    public StorIOSQLite storIOSQLite;

    public DataManager(Context context){
        init(context);
    }


    private void init(Context context){
        retrofit = new Retrofit.Builder()
                .baseUrl(AppConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        restService = retrofit.create(RestService.class);
        storIOSQLite = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(new MapOfMemoryOpenHelper(context))
                .addTypeMapping(MonumentEntity.class, SQLiteTypeMapping.<MonumentEntity>builder()
                        .putResolver(new MonumentEntityStorIOSQLitePutResolver())
                        .getResolver(new MonumentEntityStorIOSQLiteGetResolver())
                        .deleteResolver(new MonumentEntityStorIOSQLiteDeleteResolver())
                        .build())
                .addTypeMapping(PlaceEntity.class, SQLiteTypeMapping.<PlaceEntity>builder()
                        .putResolver(new PlaceEntityStorIOSQLitePutResolver())
                        .getResolver(new PlaceEntityStorIOSQLiteGetResolver())
                        .deleteResolver(new PlaceEntityStorIOSQLiteDeleteResolver())
                        .build())
                .addTypeMapping(DayOfMemory.class, SQLiteTypeMapping.<DayOfMemory>builder()
                        .putResolver(new DayOfMemoryStorIOSQLitePutResolver())
                        .getResolver(new DayOfMemoryStorIOSQLiteGetResolver())
                        .deleteResolver(new DayOfMemoryStorIOSQLiteDeleteResolver())
                        .build())
                .addTypeMapping(AboutEntity.class, SQLiteTypeMapping.<AboutEntity>builder()
                        .putResolver(new AboutEntityStorIOSQLitePutResolver())
                        .getResolver(new AboutEntityStorIOSQLiteGetResolver())
                        .deleteResolver(new AboutEntityStorIOSQLiteDeleteResolver())
                        .build())
                .addTypeMapping(RouteEntity.class, SQLiteTypeMapping.<RouteEntity>builder()
                        .putResolver(new RouteEntityStorIOSQLitePutResolver())
                        .getResolver(new RouteEntityStorIOSQLiteGetResolver())
                        .deleteResolver(new RouteEntityStorIOSQLiteDeleteResolver())
                        .build())
                .addTypeMapping(PersonEntity.class, SQLiteTypeMapping.<PersonEntity>builder()
                        .putResolver(new PersonEntityStorIOSQLitePutResolver())
                        .getResolver(new PersonEntityStorIOSQLiteGetResolver())
                        .deleteResolver(new PersonEntityStorIOSQLiteDeleteResolver())
                        .build())
                .build();
        sharedPrefs = new SharedPrefs(context);
    }

    public Single<List<PlaceEntity>> getPlaces(){
        return restService.getPlaces();
    }

    public Single<MonumentResponse> getMonuments(){
        return restService.getMonuments();
    }

    public Single<AboutEntity> getAboutInfo(int placeId){
        return restService.getAboutInfo(placeId);
    }

    public Single<RouteEntity> getRouteInfo(int placeId){
        return restService.getRouteInfo(placeId);
    }

    public Single<List<DayOfMemory>> getDOM(int placeId){
        return restService.getDOM(placeId);
    }
}
