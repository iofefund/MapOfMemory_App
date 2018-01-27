package org.mapofmemory.screens.main;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.pushtorefresh.storio3.sqlite.queries.Query;

import org.mapofmemory.AppMvpPresenter;
import org.mapofmemory.entities.PlaceEntity;
import org.mapofmemory.entities.PlaceEntityTable;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by The Tronuo on 27.01.2018.
 */

public class MainPresenter extends AppMvpPresenter<MainView> {
    private int placeId;
    private List<PlaceEntity> places;

    public MainPresenter(Context context, int placeId){
        super(context);
        this.placeId = placeId;
    }

    public void loadPlaces(){
        this.places = mDataManager.storIOSQLite
                .get()
                .listOfObjects(PlaceEntity.class)
                .withQuery(Query.builder().table(PlaceEntityTable.NAME).build())
                .prepare()
                .executeAsBlocking();
        getView().onPlacesLoad(places);
        PlaceEntity currentPlace = Observable.fromIterable(places)
                .filter(placeEntity -> placeEntity.getId() == placeId)
                .blockingFirst();
        getView().onPlaceSelected(currentPlace);
    }

    public void changePlace(int num){
        getView().onPlaceSelected(places.get(num));
    }
}
