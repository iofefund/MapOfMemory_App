package org.mapofmemory.screens.monument;

import android.content.Context;
import android.util.Log;

import com.pushtorefresh.storio3.sqlite.queries.Query;

import org.mapofmemory.AppMvpPresenter;
import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.entities.MonumentEntityTable;

/**
 * Created by The Tronuo on 29.01.2018.
 */

public class MonumentPresenter extends AppMvpPresenter<MonumentView> {
    private String monumentId;

    public MonumentPresenter(Context context, String monumentId){
        super(context);
        this.monumentId = monumentId;
    }

    public void loadMonument(){
        getView().onLoadMonument(mDataManager.storIOSQLite
                .get()
                .object(MonumentEntity.class)
                .withQuery(Query.builder().table(MonumentEntityTable.NAME).where("num = ?").whereArgs(monumentId).build())
                .prepare()
                .executeAsBlocking());
    }
}
