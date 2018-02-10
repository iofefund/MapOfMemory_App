package org.mapofmemory.screens.dom;

import android.content.Context;

import com.pushtorefresh.storio3.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio3.sqlite.queries.Query;

import org.mapofmemory.AppMvpPresenter;
import org.mapofmemory.entities.DayOfMemory;
import org.mapofmemory.entities.DayOfMemoryTable;
import org.mapofmemory.entities.PlaceEntity;
import org.mapofmemory.entities.PlaceEntityTable;
import org.ocpsoft.prettytime.units.Day;

import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by The Tronuo on 04.02.2018.
 */

public class DOMPresenter extends AppMvpPresenter<DOMView> {
    private int placeId;
    private PlaceEntity placeEntity;

    public PlaceEntity getPlaceEntity() {
        return placeEntity;
    }

    public DOMPresenter(Context context, int placeId){
        super(context);
        this.placeId = placeId;
    }

    public void loadDayOfMemories(){
        placeEntity = mDataManager.storIOSQLite
                .get()
                .object(PlaceEntity.class)
                .withQuery(Query.builder().table(PlaceEntityTable.NAME).where("id = ?").whereArgs(placeId).build())
                .prepare()
                .executeAsBlocking();
        getView().onPlaceLoad(placeEntity);
        mDataManager.getDOM(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(doms -> {
                    cacheDOMs(doms);
                    getView().onDOMLoad(doms);
                }, errors -> {
                    List<DayOfMemory> doms = fromCache();
                    if (doms.size() > 0){
                        getView().onDOMLoad(doms);
                    }
                    else {
                        getView().onDOMFailure();
                    }
                });
    }
    private void cacheDOMs(List<DayOfMemory> doms){
        mDataManager.storIOSQLite
                .delete()
                .byQuery(DeleteQuery.builder()
                        .table(DayOfMemoryTable.NAME).build()
                )
                .prepare()
                .executeAsBlocking();

        mDataManager.storIOSQLite
                .put()
                .objects(doms)
                .prepare()
                .executeAsBlocking();
    }

    private List<DayOfMemory> fromCache(){
        List<DayOfMemory> doms = mDataManager.storIOSQLite
                .get()
                .listOfObjects(DayOfMemory.class)
                .withQuery(Query.builder().table(DayOfMemoryTable.NAME).where("place_id = ?").whereArgs(placeId).build())
                .prepare()
                .executeAsBlocking();
        return doms;
    }
}
