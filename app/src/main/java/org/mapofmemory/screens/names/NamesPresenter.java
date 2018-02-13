package org.mapofmemory.screens.names;

import android.content.Context;
import android.util.Log;

import com.pushtorefresh.storio3.sqlite.queries.Query;

import org.mapofmemory.AppConfig;
import org.mapofmemory.AppMvpPresenter;
import org.mapofmemory.adapters.MonumentAdapter;
import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.entities.MonumentEntityTable;
import org.mapofmemory.entities.MonumentImgEntity;
import org.mapofmemory.entities.PersonEntity;
import org.mapofmemory.entities.PersonEntityTable;
import org.mapofmemory.entities.PersonInfo;
import org.mapofmemory.entities.PlaceEntity;
import org.mapofmemory.entities.PlaceEntityTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by The Tronuo on 05.02.2018.
 */

public class NamesPresenter extends AppMvpPresenter<NamesView> {
    private int placeId;
    PlaceEntity place;
    private List<MonumentEntity> monuments;
    public NamesPresenter(Context context, int placeId){
        super(context);
        this.placeId = placeId;
    }

    public void loadPersons(){
        Observable.just(1)
                .subscribeOn(Schedulers.io())
                .map(res ->{
                    place = mDataManager.storIOSQLite
                            .get()
                            .object(PlaceEntity.class)
                            .withQuery(Query.builder().table(PlaceEntityTable.NAME).where("id = ?").whereArgs(placeId).build())
                            .prepare()
                            .executeAsBlocking();
                    monuments = mDataManager.storIOSQLite
                            .get()
                            .listOfObjects(MonumentEntity.class)
                            .withQuery(Query.builder().table(MonumentEntityTable.NAME).where("place_id = ?").whereArgs(placeId).build())
                            .prepare()
                            .executeAsBlocking();
                    List<PersonEntity> persons = mDataManager.storIOSQLite
                            .get()
                            .listOfObjects(PersonEntity.class)
                            .withQuery(Query.builder().table(PersonEntityTable.NAME).where("place_id = ?").whereArgs(place.getId()).build())
                            .prepare()
                            .executeAsBlocking();
                    List<PersonEntity> p = new ArrayList<PersonEntity>(persons);
                    Collections.sort(p, (o1, o2) -> o1.getName().compareTo(o2.getName()));
                    return Observable.fromIterable(p).map(personEntity -> {
                        personEntity.setImg(place.getImgRoot().replace("monument", "biography") + personEntity.getImg());
                        return personEntity;
                    }).toList().blockingGet();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(personInfos -> getView().onPersonLoad(personInfos));
    }

    public List<MonumentEntity> getMonuments() {
        return monuments;
    }
}
