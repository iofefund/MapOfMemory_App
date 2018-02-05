package org.mapofmemory.screens.about;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.mapofmemory.entities.AboutEntity;

/**
 * Created by The Tronuo on 04.02.2018.
 */

public interface AboutView extends MvpView{
    void onAboutLoaded(AboutEntity aboutEntity);
    void onAboutFailure();
}
