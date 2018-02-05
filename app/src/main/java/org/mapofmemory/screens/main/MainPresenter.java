package org.mapofmemory.screens.main;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.pushtorefresh.storio3.sqlite.queries.Query;

import org.mapofmemory.AppMvpPresenter;
import org.mapofmemory.entities.PlaceEntity;
import org.mapofmemory.entities.PlaceEntityTable;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by The Tronuo on 27.01.2018.
 */

public class MainPresenter extends AppMvpPresenter<MainView> {
    private int placeId;
    private List<PlaceEntity> places;
    private PlaceEntity currentPlace;
    public MainPresenter(Context context, int placeId){
        super(context);
        this.placeId = placeId;
    }

    public void loadPlaces(){
        mDataManager.storIOSQLite
                .get()
                .listOfObjects(PlaceEntity.class)
                .withQuery(Query.builder().table(PlaceEntityTable.NAME).build())
                .prepare()
                .asRxSingle()
                .subscribeOn(Schedulers.io())
                .map(places -> {
                    this.places = places;
                    currentPlace = Observable.fromIterable(places)
                            .filter(placeEntity -> placeEntity.getId() == placeId)
                            .blockingFirst();
                    return currentPlace;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(currentPlace -> {
                    getView().onPlacesLoad(places, currentPlace);
                    getView().onPlaceSelected(currentPlace, places.indexOf(currentPlace));
                    getView().onMapFragment(currentPlace.getLat(), currentPlace.getLng());
                });
    }

    public void changePlace(int num){
        placeId = places.get(num).getId();
        getView().onPlaceSelected(places.get(num), num);
    }

    public int getPlaceId() {
        return placeId;
    }

    public PlaceEntity getCurrentPlace() {
        return currentPlace;
    }
}
