package org.mapofmemory;

import android.content.Context;

import com.pushtorefresh.storio3.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio3.sqlite.StorIOSQLite;
import com.pushtorefresh.storio3.sqlite.impl.DefaultStorIOSQLite;

import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.entities.MonumentEntityStorIOSQLiteDeleteResolver;
import org.mapofmemory.entities.MonumentEntityStorIOSQLiteGetResolver;
import org.mapofmemory.entities.MonumentEntityStorIOSQLitePutResolver;
import org.mapofmemory.entities.MonumentImgEntity;
import org.mapofmemory.entities.PlaceEntity;
import org.mapofmemory.entities.PlaceEntityStorIOSQLiteDeleteResolver;
import org.mapofmemory.entities.PlaceEntityStorIOSQLiteGetResolver;
import org.mapofmemory.entities.PlaceEntityStorIOSQLitePutResolver;

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
                .build();
    }

    public Single<List<PlaceEntity>> getPlaces(){
        return restService.getPlaces();
    }

    public Single<List<MonumentEntity>> getMonuments(){
        return restService.getMonuments();
    }
}
