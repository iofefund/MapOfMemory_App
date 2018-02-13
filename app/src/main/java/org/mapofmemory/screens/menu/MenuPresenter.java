package org.mapofmemory.screens.menu;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pushtorefresh.storio3.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio3.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio3.sqlite.queries.Query;
import com.pushtorefresh.storio3.sqlite.queries.RawQuery;

import org.mapofmemory.AppMvpPresenter;
import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.entities.MonumentEntityTable;
import org.mapofmemory.entities.PersonEntity;
import org.mapofmemory.entities.PersonEntityTable;
import org.mapofmemory.entities.PlaceEntity;
import org.mapofmemory.entities.PlaceEntityTable;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;

/**
 * Created by The Tronuo on 23.01.2018.
 */

public class MenuPresenter extends AppMvpPresenter<MenuView>{
    public MenuPresenter(Context context){
        super(context);
    }

    public void loadPlaces(){
        mDataManager.getPlaces()
                .observeOn(mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(places -> {
                    getView().showPlaces(places);
                    cachePlaces(places);
                    loadMonuments();
                }, errors -> {
                    if (mDataManager.sharedPrefs.contains("update_date")){
                        long date = mDataManager.sharedPrefs.getLong("update_date");
                        getView().onDataFailed(new PrettyTime().format(new Date(date)));
                        getView().showPlaces(placesFromCache());
                    }
                    else{
                        getView().onDataFailed("Никогда");
                    }

                });
    }

    public void loadMonuments(){
        mDataManager.getMonuments()
                .observeOn(mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(monuments -> {
                    cacheMonuments(monuments.getMonuments());
                    cachePersons(monuments.getPersons());
                });
    }

    private List<PlaceEntity> placesFromCache(){
        return mDataManager.storIOSQLite
                .get()
                .listOfObjects(PlaceEntity.class)
                .withQuery(Query.builder().table(PlaceEntityTable.NAME).build())
                .prepare()
                .executeAsBlocking();
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

    private void cachePersons(List<PersonEntity> persons){
        mDataManager.storIOSQLite
                .delete()
                .byQuery(DeleteQuery.builder()
                        .table(PersonEntityTable.NAME).build()
                )
                .prepare()
                .executeAsBlocking();

        mDataManager.storIOSQLite
                .put()
                .objects(persons)
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
                .objects(Observable.fromIterable(monuments)
                        .map(monumentEntity -> {
                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            monumentEntity.setImgs_json(gson.toJson(monumentEntity.getImgs()));
                            monumentEntity.setBiographyIdsJson(gson.toJson(monumentEntity.getBiographyIds()));
                            return monumentEntity;
                        })
                        .toList()
                        .blockingGet()
                )
                .prepare()
                .executeAsBlocking();
        long date = System.currentTimeMillis();
        mDataManager.sharedPrefs.saveLong("update_date", date);
        PrettyTime p = new PrettyTime();
        getView().onDataSuccess(p.format(new Date(date)));
    }
}
