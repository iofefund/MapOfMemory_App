package org.mapofmemory.screens.monument;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.mapofmemory.entities.MonumentEntity;

/**
 * Created by The Tronuo on 29.01.2018.
 */

public interface MonumentView extends MvpView {
    void onLoadMonument(MonumentEntity monumentEntity);
}
