package org.mapofmemory.screens.map;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.pushtorefresh.storio3.sqlite.queries.Query;

import org.mapofmemory.AppMvpPresenter;
import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.entities.MonumentEntityTable;

/**
 * Created by The Tronuo on 27.01.2018.
 */

public class MapPresenter extends AppMvpPresenter<MapView> {
    public MapPresenter(Context context){
        super(context);
    }

    public void loadMonuments(){
        getView().showMonuments(mDataManager.storIOSQLite
                .get()
                .listOfObjects(MonumentEntity.class)
                .withQuery(Query.builder().table(MonumentEntityTable.NAME).where("lat <> '' AND lng <> ''").limit(50).build())
                .prepare()
                .executeAsBlocking());
    }
}
