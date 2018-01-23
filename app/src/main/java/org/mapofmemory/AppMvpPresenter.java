package org.mapofmemory;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

/**
 * Created by The Tronuo on 21.01.2018.
 */

public class AppMvpPresenter<V extends MvpView> extends MvpBasePresenter<V> {
    public DataManager mDataManager;
    public AppMvpPresenter(){
        mDataManager = new DataManager();
    }
}
