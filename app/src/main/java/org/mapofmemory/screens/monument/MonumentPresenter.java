package org.mapofmemory.screens.monument;

import android.content.Context;
import android.util.Log;

import com.pushtorefresh.storio3.sqlite.queries.Query;

import org.mapofmemory.AppMvpPresenter;
import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.entities.MonumentEntityTable;
import org.mapofmemory.entities.PlaceEntity;
import org.mapofmemory.entities.PlaceEntityTable;

/**
 * Created by The Tronuo on 29.01.2018.
 */

public class MonumentPresenter extends AppMvpPresenter<MonumentView> {
    private String monumentId;
    private MonumentEntity monumentEntity;
    private PlaceEntity place;

    public MonumentEntity getMonumentEntity() {
        return monumentEntity;
    }

    public MonumentPresenter(Context context, String monumentId){
        super(context);
        this.monumentId = monumentId;
    }

    public PlaceEntity getPlace() {
        return place;
    }

    public void loadMonument(){
        this.monumentEntity = mDataManager.storIOSQLite
                .get()
                .object(MonumentEntity.class)
                .withQuery(Query.builder().table(MonumentEntityTable.NAME).where("num = ?").whereArgs(monumentId).build())
                .prepare()
                .executeAsBlocking();
        getView().onLoadMonument(monumentEntity);
        place = mDataManager.storIOSQLite
                .get()
                .object(PlaceEntity.class)
                .withQuery(Query.builder().table(PlaceEntityTable.NAME).where("id = ?").whereArgs(monumentEntity.getPlaceId()).build())
                .prepare()
                .executeAsBlocking();

    }
}
