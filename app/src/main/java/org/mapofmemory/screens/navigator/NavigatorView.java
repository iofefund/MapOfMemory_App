package org.mapofmemory.screens.navigator;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.mapofmemory.entities.MonumentEntity;

/**
 * Created by The Tronuo on 04.02.2018.
 */

public interface NavigatorView extends MvpView {
    void onLoadMonument(MonumentEntity monumentEntity, String imgRoot);
}
