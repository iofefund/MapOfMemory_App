package org.mapofmemory.screens.dom;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.mapofmemory.entities.DayOfMemory;
import org.mapofmemory.entities.PlaceEntity;

import java.util.List;

/**
 * Created by The Tronuo on 04.02.2018.
 */

public interface DOMView extends MvpView{
    void onPlaceLoad(PlaceEntity place);
    void onDOMLoad(List<DayOfMemory> doms);
    void onDOMFailure();
}
