package org.mapofmemory.screens.menu;

import android.util.Log;

import org.mapofmemory.AppMvpPresenter;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by The Tronuo on 23.01.2018.
 */

public class MenuPresenter extends AppMvpPresenter<MenuView>{
    public MenuPresenter(){
        super();
    }

    public void loadPlaces(){
        mDataManager.getPlaces()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(places -> getView().showPlaces(places));
    }
}
