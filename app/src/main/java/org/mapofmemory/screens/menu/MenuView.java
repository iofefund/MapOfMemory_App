package org.mapofmemory.screens.menu;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.mapofmemory.entities.PlaceEntity;

import java.util.List;

/**
 * Created by The Tronuo on 23.01.2018.
 */

public interface MenuView extends MvpView {
    void showPlaces(List<PlaceEntity> places);
    void onDataSuccess(String date);
    void onDataFailed(String date);
}
