package org.mapofmemory.screens.main;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.mapofmemory.entities.PlaceEntity;

import java.util.List;

/**
 * Created by The Tronuo on 27.01.2018.
 */

public interface MainView extends MvpView{
    void onPlacesLoad(List<PlaceEntity> places);
    void onPlaceSelected(PlaceEntity place);
}
