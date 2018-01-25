package org.mapofmemory.screens.menu;

import android.content.Context;
import android.util.Log;

import com.pushtorefresh.storio3.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio3.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio3.sqlite.queries.RawQuery;

import org.mapofmemory.AppMvpPresenter;
import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.entities.MonumentEntityTable;
import org.mapofmemory.entities.PlaceEntity;
import org.mapofmemory.entities.PlaceEntityTable;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by The Tronuo on 23.01.2018.
 */

public class MenuPresenter extends AppMvpPresenter<MenuView>{
    public MenuPresenter(Context context){
        super(context);
    }

    public void loadPlaces(){
        mDataManager.getPlaces()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(places -> {
                    getView().showPlaces(places);
                    cachePlaces(places);
                    loadMonuments(places);
                });
    }

    public void loadMonuments(List<PlaceEntity> places){
        List<Integer> ids = Observable.fromIterable(places)
                .map(placeEntity -> placeEntity.getId())
                .toList()
                .blockingGet();
        List<Single<List<MonumentEntity>>> singles = new ArrayList<>();
        for (Integer id: ids){
            singles.add(mDataManager.getMonuments(id + ""));
        }
        Single.concat(singles)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(monuments -> Log.d("MONUMENTS", monuments.size() + "!"));


    }

    private void cachePlaces(List<PlaceEntity> places){
        mDataManager.storIOSQLite
                .delete()
                .byQuery(DeleteQuery.builder()
                        .table(PlaceEntityTable.NAME).build()
                )
                .prepare()
                .executeAsBlocking();

        mDataManager.storIOSQLite
                .put()
                .objects(places)
                .prepare()
                .executeAsBlocking();
    }

    private void cacheMonuments(List<MonumentEntity> monuments){
        mDataManager.storIOSQLite
                .delete()
                .byQuery(DeleteQuery.builder()
                        .table(MonumentEntityTable.NAME).build()
                )
                .prepare()
                .executeAsBlocking();

        mDataManager.storIOSQLite
                .put()
                .objects(monuments)
                .prepare()
                .executeAsBlocking();
    }
}
