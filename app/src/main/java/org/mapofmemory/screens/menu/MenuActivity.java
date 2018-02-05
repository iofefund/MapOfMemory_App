package org.mapofmemory.screens.menu;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.mapofmemory.R;
import org.mapofmemory.adapters.PlaceEntityAdapter;
import org.mapofmemory.entities.PlaceEntity;
import org.mapofmemory.screens.main.MainActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MenuActivity extends MvpActivity<MenuView, MenuPresenter> implements MenuView, PermissionListener {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.status) TextView status;
    private boolean isGranted = false;
    private boolean isDataUpdated = false;
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
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(this).check();
        ButterKnife.bind(this);
        status.setText("Идет обновление данных...");
        getPresenter().loadPlaces();
    }

    @Override
    public void showPlaces(List<PlaceEntity> places) {
        //places.addAll(places);
        isDataUpdated = true;
        PlaceEntityAdapter adapter = new PlaceEntityAdapter(places);
        adapter.setOnPlaceClickListener(place -> openPlace(place.getId()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void openPlace(int id){
        if (isGranted && isDataUpdated) {
            Intent newInt = new Intent(this, MainActivity.class);
            newInt.putExtra("place_id", id);
            startActivity(newInt);
        }
    }

    @Override
    public void onDataSuccess(String date) {
        status.setText("Данные обновлены\n" + date);
        isDataUpdated = true;
        //Toast.makeText(this, "Данные обновлены!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDataFailed(String date) {
        status.setText("Данные обновлены\n" + date);
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
        isGranted = true;
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {

    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

    }
}
