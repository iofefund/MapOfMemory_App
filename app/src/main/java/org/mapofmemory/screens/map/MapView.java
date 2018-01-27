package org.mapofmemory.screens.map;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.mapofmemory.entities.MonumentEntity;

import java.util.List;

/**
 * Created by The Tronuo on 27.01.2018.
 */

public interface MapView extends MvpView {
    void showMonuments(List<MonumentEntity> monuments);
}
