package org.mapofmemory.screens.names;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import org.mapofmemory.entities.PersonEntity;
import org.mapofmemory.entities.PersonInfo;

import java.util.List;

/**
 * Created by The Tronuo on 05.02.2018.
 */

public interface NamesView extends MvpView {
    void onPersonLoad(List<PersonEntity> persons);
}
