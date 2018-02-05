package org.mapofmemory.screens.route;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.mapofmemory.entities.PlaceEntity;
import org.mapofmemory.entities.RouteEntity;

/**
 * Created by The Tronuo on 04.02.2018.
 */

public interface RouteView extends MvpView {
    void onRouteLoaded(RouteEntity routeEntity);
    void onRouteFailure();
    void onPlaceLoaded(PlaceEntity place, RouteEntity routeEntity);
}
