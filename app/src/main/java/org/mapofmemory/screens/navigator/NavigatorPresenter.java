package org.mapofmemory.screens.navigator;

import android.content.Context;

import com.pushtorefresh.storio3.sqlite.queries.Query;

import org.mapofmemory.AppMvpPresenter;
import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.entities.MonumentEntityTable;
import org.mapofmemory.entities.PlaceEntity;
import org.mapofmemory.entities.PlaceEntityTable;

import java.util.Observable;
import java.util.concurrent.TimeUnit;

/**
 * Created by The Tronuo on 04.02.2018.
 */

public class NavigatorPresenter extends AppMvpPresenter<NavigatorView> {
    private String id;
    private MonumentEntity monument;
    private PlaceEntity place;

    public NavigatorPresenter(Context context, String id){
        super(context);
        this.id = id;
    }
    public void loadMonument(){
        this.monument = mDataManager.storIOSQLite
                .get()
                .object(MonumentEntity.class)
                .withQuery(Query.builder().table(MonumentEntityTable.NAME).where("num = ?").whereArgs(id).build())
                .prepare()
                .executeAsBlocking();
        this.place = mDataManager.storIOSQLite
                .get()
                .object(PlaceEntity.class)
                .withQuery(Query.builder().table(PlaceEntityTable.NAME).where("id = ?").whereArgs(monument.getPlaceId()).build())
                .prepare()
                .executeAsBlocking();
        getView().onLoadMonument(monument, place.getImgRoot());
    }

    public PlaceEntity getPlace() {
        return place;
    }

    public MonumentEntity getMonument() {
        return monument;
    }
}
