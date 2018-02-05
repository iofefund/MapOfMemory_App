package org.mapofmemory.screens.main;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.mapofmemory.entities.PlaceEntity;

import java.util.List;

/**
 * Created by The Tronuo on 27.01.2018.
 */

public interface MainView extends MvpView{
    void onPlacesLoad(List<PlaceEntity> places, PlaceEntity currentPlace);
    void onPlaceSelected(PlaceEntity place, int index);
    void onMapFragment(double lat, double lng);
    void onAboutFragment(int placeId);
    void onPersonsFragment(int placeId);
    void onRouteFragment(int placeId);
    void onDOMFragment(int placeId);
    void onSearchExpand();
}
