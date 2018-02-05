package org.mapofmemory.screens.about;

import android.content.Context;

import com.pushtorefresh.storio3.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio3.sqlite.queries.Query;

import org.mapofmemory.AppMvpPresenter;
import org.mapofmemory.entities.AboutEntity;
import org.mapofmemory.entities.AboutEntityTable;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by The Tronuo on 04.02.2018.
 */

public class AboutPresenter extends AppMvpPresenter<AboutView> {
    private int placeId;
    public AboutPresenter(Context context, int placeId){
        super(context);
        this.placeId = placeId;
    }
    public void loadAboutInfo(){
        mDataManager.getAboutInfo(placeId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aboutEntity -> {
                    cacheAbout(aboutEntity);
                    getView().onAboutLoaded(aboutEntity);
                }, errors -> {
                    try{
                        AboutEntity aboutEntity = fromCache();
                        if (aboutEntity != null){
                            getView().onAboutLoaded(aboutEntity);
                        }
                        else{
                            getView().onAboutFailure();
                        }
                    }
                    catch (Exception e){
                        getView().onAboutFailure();
                    }
                });
    }

    public void cacheAbout(AboutEntity aboutEntity){
        mDataManager.storIOSQLite
                .delete()
                .byQuery(DeleteQuery.builder().table(AboutEntityTable.NAME).where("place_id = ?").whereArgs(placeId).build())
                .prepare()
                .executeAsBlocking();

        mDataManager.storIOSQLite
                .put()
                .object(aboutEntity)
                .prepare()
                .executeAsBlocking();
    }

    public AboutEntity fromCache(){
        return mDataManager.storIOSQLite
                .get()
                .object(AboutEntity.class)
                .withQuery(Query.builder().table(AboutEntityTable.NAME).where("place_id = ?").whereArgs(placeId).build())
                .prepare()
                .executeAsBlocking();
    }
}
