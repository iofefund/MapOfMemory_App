package org.mapofmemory.screens.map;

import android.content.Context;

import com.pushtorefresh.storio3.sqlite.queries.Query;

import org.mapofmemory.AppMvpPresenter;
import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.entities.MonumentEntityTable;
import org.mapofmemory.entities.PlaceEntity;
import org.mapofmemory.entities.PlaceEntityTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

    public void loadMonuments(){
        mDataManager.storIOSQLite
                .get()
                .listOfObjects(MonumentEntity.class)
                .withQuery(Query.builder().table(MonumentEntityTable.NAME).where("lat <> '' AND lng <> '' AND place_id = ?").whereArgs(placeId).build())
                .prepare()
                .asRxSingle()
                .subscribeOn(Schedulers.io())
                .filter(monumentEntities -> {
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

    }

    @Override
    public void attachView(MapView view) {
        super.attachView(view);

    }

    public void showSearch(){
        getView().showSearch(monuments);
    }
}
