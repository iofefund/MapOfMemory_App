package org.mapofmemory.screens.names;

import android.content.Context;
import android.util.Log;

import com.pushtorefresh.storio3.sqlite.queries.Query;

import org.mapofmemory.AppConfig;
import org.mapofmemory.AppMvpPresenter;
import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.entities.MonumentEntityTable;
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
    private PlaceEntity place;
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
                    List<MonumentEntity> monuments = mDataManager.storIOSQLite
                            .get()
                            .listOfObjects(MonumentEntity.class)
                            .withQuery(Query.builder().table(MonumentEntityTable.NAME).where("place_id = ? AND type = ?").whereArgs(placeId, "1").build())
                            .prepare()
                            .executeAsBlocking();
                    List<PersonInfo> personInfos = Observable.fromIterable(monuments)
                            .filter(monumentEntity -> !monumentEntity.getRealName().isEmpty())
                            .toList()
                            .map(monumentEntities -> {
                                List<PersonInfo> infos = new ArrayList<>();
                                List<String> persons = Observable.fromIterable(monumentEntities)
                                        .map(monumentEntity -> monumentEntity.getRealName())
                                        .toList()
                                        .blockingGet();
                                for (MonumentEntity monumentEntity: monumentEntities){
                                    if (monumentEntity.getRealName().contains(", ")){
                                        String[] names = monumentEntity.getRealName().split(", ");
                                        for (String name : names){
                                            PersonInfo personInfo = new PersonInfo();
                                            if (monumentEntity.getImgs().size() > 0) personInfo.setImage(place.getImgRoot() + monumentEntity.getImgs().get(0).getImg());
                                            personInfo.setName(name.trim());
                                            personInfo.setType(monumentEntity.getType2());
                                            personInfo.setNum(monumentEntity.getNum());
                                            infos.add(personInfo);
                                        }
                                    }
                                    else{
                                        PersonInfo personInfo = new PersonInfo();
                                        if (monumentEntity.getImgs().size() > 0) personInfo.setImage(place.getImgRoot() + monumentEntity.getImgs().get(0).getImg());
                                        if (monumentEntity.getRealName().contains(", ")){
                                            personInfo.setName(monumentEntity.getRealName().replaceAll(", ", "\n"));
                                        }
                                        else{
                                            personInfo.setName(monumentEntity.getRealName());
                                        }
                                        personInfo.setType(monumentEntity.getType2());
                                        personInfo.setNum(monumentEntity.getNum());
                                        infos.add(personInfo);
                                    }
                                }
                                return infos;
                            })
                            .blockingGet();
                    Collections.sort(personInfos, new Comparator<PersonInfo>() {
                        @Override
                        public int compare(PersonInfo o1, PersonInfo o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                    Log.d("PERSONS", AppConfig.getDuplicates(personInfos).toString());
                    List<String> duplicates = AppConfig.getDuplicates(personInfos);
                    for (String duplicate : duplicates){
                        List<PersonInfo> persons = new ArrayList<>();
                        persons.addAll(personInfos);
                        List<PersonInfo> innerPersons = Observable.fromIterable(persons)
                                .filter(personInfo1 -> personInfo1.getName().equals(duplicate))
                                .map(personInfo1 -> {
                                    //personInfo1.setName(personInfo1.getType());
                                    return personInfo1;
                                })
                                .toList()
                                .blockingGet();
                        try {
                            personInfos.get(AppConfig.findPersonInfoByName(personInfos, duplicate)).setInners(innerPersons);
                        }
                        catch (Exception e){

                        }
                    }
                    return AppConfig.removePersonDuplicates(personInfos);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(personInfos -> getView().onPersonLoad(personInfos));
    }
}
