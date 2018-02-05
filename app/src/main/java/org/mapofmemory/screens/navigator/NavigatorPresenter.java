package org.mapofmemory.screens.navigator;

import android.content.Context;

import com.pushtorefresh.storio3.sqlite.queries.Query;

import org.mapofmemory.AppMvpPresenter;
import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.entities.MonumentEntityTable;

/**
 * Created by The Tronuo on 04.02.2018.
 */

public class NavigatorPresenter extends AppMvpPresenter<NavigatorView> {
    private String id;
    private MonumentEntity monument;

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
        getView().onLoadMonument(monument);
    }

    public MonumentEntity getMonument() {
        return monument;
    }
}
