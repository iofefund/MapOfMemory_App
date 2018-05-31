package org.mapofmemory.screens.menu;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.mapofmemory.FileManager;
import org.mapofmemory.R;
import org.mapofmemory.adapters.PlaceEntityAdapter;
import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.entities.PlaceEntity;
import org.mapofmemory.screens.main.MainActivity;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MenuActivity extends MvpActivity<MenuView, MenuPresenter> implements MenuView, MultiplePermissionsListener {
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.status) TextView status;

    @OnClick(R.id.btn_update) void onUpdate(){
        status.setText("Идет обновление данных...");
        getPresenter().loadPlaces();
    }
    private boolean isGranted = false;
    private boolean isDataUpdated = false;
    @NonNull
    @Override
    public MenuPresenter createPresenter() {
        return new MenuPresenter(getApplicationContext());
    }

    @Override
    public void onDataUpdating() {
        status.setText("Идет обновление данных...");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();


        Dexter.withActivity(this)
                .withPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION})
                .withListener(this).check();
        ButterKnife.bind(this);
        getPresenter().loadMonumentsFromCache();
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
        status.setText("Данные обновлены: " + date);
        //
        if (isGranted) {
            Observable.just(1)
                    .subscribeOn(Schedulers.io())
                    .filter(res -> {
                        FileManager fileManager = new FileManager(getAssets());
                        fileManager.copyFileIfNeeded("cache.db");
                        return true;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(res -> {
                        isDataUpdated = true;
                    });
        }
        // isDataUpdated = true;
        //Toast.makeText(this, "Данные обновлены!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDataFailed(String date) {
        status.setText("Данные обновлены: " + date);
    }



    @Override
    public void onPermissionsChecked(MultiplePermissionsReport report) {
        isGranted = report.areAllPermissionsGranted();
        if(isGranted) {
            Observable.just(1)
                    .subscribeOn(Schedulers.io())
                    .filter(res -> {
                        FileManager fileManager = new FileManager(getAssets());
                        fileManager.copyFileIfNeeded("cache.db");
                        return true;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(res -> {
                        isDataUpdated = true;
                    });
        }
    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

    }
}
