package org.mapofmemory.screens.route;

import android.content.Context;

import com.pushtorefresh.storio3.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio3.sqlite.queries.Query;

import org.mapofmemory.AppMvpPresenter;
import org.mapofmemory.entities.PlaceEntity;
import org.mapofmemory.entities.PlaceEntityTable;
import org.mapofmemory.entities.RouteEntity;
import org.mapofmemory.entities.RouteEntityTable;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by The Tronuo on 04.02.2018.
 */

public class RoutePresenter extends AppMvpPresenter<RouteView> {
    private int placeId;
    private RouteEntity routeEntity;

    public RoutePresenter(Context context, int placeId){
        super(context);
        this.placeId = placeId;
    }

    public void loadRoute(){
        mDataManager.getRouteInfo(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(route -> {
                    this.routeEntity = route;
                    cacheRoute(route);
                    getView().onRouteLoaded(route);
                    getView().onPlaceLoaded(mDataManager.storIOSQLite
                            .get()
                            .object(PlaceEntity.class)
                            .withQuery(Query.builder().table(PlaceEntityTable.NAME).where("id = ?").whereArgs(placeId).build())
                            .prepare()
                            .executeAsBlocking(), routeEntity);
                }, errors -> {
                    try{
                        RouteEntity routeEntity = fromCache();
                        if (routeEntity != null){
                            getView().onRouteLoaded(routeEntity);
                            getView().onPlaceLoaded(mDataManager.storIOSQLite
                                    .get()
                                    .object(PlaceEntity.class)
                                    .withQuery(Query.builder().table(PlaceEntityTable.NAME).where("id = ?").whereArgs(placeId).build())
                                    .prepare()
                                    .executeAsBlocking(), routeEntity);
                        }
                        else{
                            getView().onRouteFailure();
                        }
                    }
                    catch (Exception e){
                        getView().onRouteFailure();

                    }
                });
    }

    public void cacheRoute(RouteEntity routeEntity){
        mDataManager.storIOSQLite
                .delete()
                .byQuery(DeleteQuery.builder().table(RouteEntityTable.NAME).where("place_id = ?").whereArgs(placeId).build())
                .prepare()
                .executeAsBlocking();

        mDataManager.storIOSQLite
                .put()
                .object(routeEntity)
                .prepare()
                .executeAsBlocking();
    }

    public RouteEntity fromCache(){
        return mDataManager.storIOSQLite
                .get()
                .object(RouteEntity.class)
                .withQuery(Query.builder().table(RouteEntityTable.NAME).where("place_id = ?").whereArgs(placeId).build())
                .prepare()
                .executeAsBlocking();
    }
}
