package org.mapofmemory.screens.map;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.pushtorefresh.storio3.sqlite.queries.Query;

import org.mapofmemory.AppMvpPresenter;
import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.entities.MonumentEntityTable;
import org.mapofmemory.entities.PlaceEntity;
import org.mapofmemory.entities.PlaceEntityTable;
import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by The Tronuo on 27.01.2018.
 */

public class MapPresenter extends AppMvpPresenter<MapView> {
    private int placeId;
    public PlaceEntity place;
    private List<MonumentEntity> monuments = new ArrayList<>();
    private MapFragment mapFragment;
    public MapPresenter(MapFragment mapFragment, Context context, int placeId){
        super(context);
        this.placeId = placeId;
        this.mapFragment = mapFragment;
    }

    public List<MonumentEntity> getMonuments() {
        return monuments;
    }
    private Disposable disp;
    public void loadMonuments(){
        Cursor c = mDataManager.storIOSQLite
                .get()
                .cursor()
                .withQuery(Query.builder().table(MonumentEntityTable.NAME).where("lat <> '' AND lng <> '' AND place_id = ?").whereArgs(placeId).build())
                .prepare()
                .executeAsBlocking();
        disp = Observable.just(1)
                .subscribeOn(Schedulers.io())
                .map(r -> {
                    List<MonumentEntity> monuments = new ArrayList<>();
                    c.moveToFirst();
                    for (int i = 0; i <= c.getCount() - 1; i++){
                        Log.d("MONUMENTS", "Retrieve " + (i + 1) + "/" + c.getCount());
                        monuments.add(new MonumentEntity(c));
                        final int progr = i + 1;
                        mapFragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isViewAttached())
                                getView().onProgress(progr, c.getCount() * 2);
                            }
                        });
                        c.moveToNext();
                    }
                    return monuments;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .filter(monumentEntities -> {
                    Log.d("FILTER", "аа");
                    this.monuments = monumentEntities;
                    this.place = mDataManager.storIOSQLite
                            .get()
                            .object(PlaceEntity.class)
                            .withQuery(Query.builder().table(PlaceEntityTable.NAME).where("id = ?").whereArgs(placeId).build())
                            .prepare()
                            .executeAsBlocking();
                    return true;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v -> mapFragment.showMonuments(monuments, place.getImgRoot()));
        /*disp = mDataManager.storIOSQLite
                .get()
                .listOfObjects(MonumentEntity.class)
                .withQuery(Query.builder().table(MonumentEntityTable.NAME).where("lat <> '' AND lng <> '' AND place_id = ?").whereArgs(placeId).build())
                .prepare()
                .asRxFlowable(BackpressureStrategy.LATEST)
                .doOnNext(new Consumer<List<MonumentEntity>>() {
                    @Override
                    public void accept(List<MonumentEntity> monumentEntities) throws Exception {
                        Log.d("MONUMENTS", "афыа");
                    }
                })
                .subscribeOn(Schedulers.io())
                .filter(monumentEntities -> {
                    Log.d("FILTER", "аа");
                    this.monuments = monumentEntities;
                    this.place = mDataManager.storIOSQLite
                            .get()
                            .object(PlaceEntity.class)
                            .withQuery(Query.builder().table(PlaceEntityTable.NAME).where("id = ?").whereArgs(placeId).build())
                            .prepare()
                            .executeAsBlocking();
                    return true;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v -> mapFragment.showMonuments(monuments, place.getImgRoot()));*/

    }

    public void dispose(){
        if (disp != null){
            disp.dispose();
        }
    }

    @Override
    public void attachView(MapView view) {
        super.attachView(view);
    }
}
