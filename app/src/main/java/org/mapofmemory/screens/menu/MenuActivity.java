package org.mapofmemory.screens.menu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;

import org.mapofmemory.R;
import org.mapofmemory.adapters.PlaceEntityAdapter;
import org.mapofmemory.entities.PlaceEntity;
import org.mapofmemory.screens.main.MainActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuActivity extends MvpActivity<MenuView, MenuPresenter> implements MenuView {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    @NonNull
    @Override
    public MenuPresenter createPresenter() {
        return new MenuPresenter(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();

        ButterKnife.bind(this);
        getPresenter().loadPlaces();
    }

    @Override
    public void showPlaces(List<PlaceEntity> places) {
       // places.addAll(places);
        PlaceEntityAdapter adapter = new PlaceEntityAdapter(places);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDataSuccess() {
        Intent newInt = new Intent(this, MainActivity.class);
        startActivity(newInt);
        Toast.makeText(this, "Данные обновлены!", Toast.LENGTH_SHORT).show();
    }
}
