package org.mapofmemory.screens.menu;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

import org.mapofmemory.R;
import org.mapofmemory.adapters.PlaceEntityAdapter;
import org.mapofmemory.entities.PlaceEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuActivity extends MvpActivity<MenuView, MenuPresenter> implements MenuView {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @NonNull
    @Override
    public MenuPresenter createPresenter() {
        return new MenuPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        ButterKnife.bind(this);
        getPresenter().loadPlaces();
    }

    @Override
    public void showPlaces(List<PlaceEntity> places) {
        PlaceEntityAdapter adapter = new PlaceEntityAdapter(places);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
