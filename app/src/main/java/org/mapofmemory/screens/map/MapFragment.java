package org.mapofmemory.screens.map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.mapofmemory.AppConfig;
import org.mapofmemory.MonumentInfoWindow;
import org.mapofmemory.R;
import org.mapofmemory.adapters.MonumentEntityAdapter;
import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.screens.main.MainActivity;
import org.mapofmemory.screens.monument.MonumentActivity;
import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by The Tronuo on 27.01.2018.
 */

public class MapFragment extends MvpFragment<MapView, MapPresenter> implements MapView, Marker.OnMarkerClickListener, RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.map) org.osmdroid.views.MapView map;
    @BindView(R.id.progress) ProgressBar progressBar;
    @BindDrawable(R.drawable.ic_blue_marker) Drawable blueMarker;
    @BindDrawable(R.drawable.ic_red_marker) Drawable redMarker;

    final private String[] radioButtonTitles = {"Все", "Личные", "Коллективные"};
    private DialogPlus dialogPlus;
    private Activity activity;
    private RadioGroup radioGroup;
    private List<MonumentEntity> monuments;

    @Override
    public MapPresenter createPresenter() {
        return new MapPresenter(this, getContext(), getArguments().getInt("place_id"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }
    public void search(){
        if (dialogPlus.isShowing()){
            dialogPlus.dismiss();
        }
        else{
            dialogPlus.show();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        progressBar.setVisibility(View.VISIBLE);
        this.activity = getActivity();
        map.setTileSource(new XYTileSource("/storage/emulated/0/Download/Sandramokh.sqlite", 0, 16, 256, ".sqlite", new String[] {}));
        map.setMultiTouchControls(true);
        map.setUseDataConnection(false);
        IMapController mapController = map.getController();
        mapController.setZoom(16);
        GeoPoint startPoint = new GeoPoint(getArguments().getDouble("lat"), getArguments().getDouble("lng"));
        mapController.setCenter(startPoint);
        dialogPlus = DialogPlus.newDialog(getActivity())
                .setContentHolder(new ViewHolder(R.layout.view_search))
                .setExpanded(true, (int)(0.75f * getActivity().getWindowManager().getDefaultDisplay().getHeight()))
                .create();
        getPresenter().loadMonuments();
        initRadioGroup();
        RecyclerView recyclerView = (RecyclerView)dialogPlus.getHolderView().findViewById(R.id.recyclerView);
    }

    List<Marker> markers = new ArrayList<>();

    @Override
    public void showMonuments(List<MonumentEntity> monuments, String imgRoot) {
        markers.clear();
        this.monuments = monuments;
        Flowable.fromIterable(monuments)
                .subscribeOn(Schedulers.io())
                .filter(monumentEntity ->{
                        Log.d("MILLIS", System.currentTimeMillis() + "");
                        GeoPoint startPoint = new GeoPoint(Double.parseDouble(monumentEntity.getLat()), Double.parseDouble(monumentEntity.getLng()));
                        Marker startMarker = new Marker(map);
                        startMarker.setOnMarkerClickListener(this);
                        startMarker.setPosition(startPoint);
                        startMarker.setTitle("Marker" + monumentEntity.getId());
                        MonumentInfoWindow monumentInfoWindow = new MonumentInfoWindow(map, monumentEntity.getImgs().size() != 0 ? imgRoot + monumentEntity.getImgs().get(0).getImg() : "", monumentEntity);
                        monumentInfoWindow.setOnWindowClickListener(window -> {
                            ImageView image = window.getImage();
                            ActivityOptionsCompat options =
                                    ActivityOptionsCompat.makeClipRevealAnimation(image, (int)image.getX(), (int)image.getY(), image.getWidth(), image.getHeight());
                            Intent newInt = new Intent(getContext(), MonumentActivity.class);
                            newInt.putExtra("image_url", window.getImageUrl());
                            newInt.putExtra("name", monumentEntity.getName());
                            newInt.putExtra("type2", monumentEntity.getType2());
                            newInt.putExtra("descr", monumentEntity.getDesc());
                            startActivity(newInt, options.toBundle());
                        });
                        startMarker.setInfoWindow(monumentInfoWindow);
                        startMarker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
                        startMarker.setIcon(monumentEntity.getType().equals("1") ? redMarker : blueMarker);
                        markers.add(startMarker);
                        Log.d("MARKER", "Added!");
                    return true;
                })
                .toList()
                .filter(v -> {map.getOverlays().addAll(markers); return true;})
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(monumentEntities -> {
                    progressBar.setVisibility(View.GONE);
                    map.invalidate();
                    map.setVisibility(View.VISIBLE);
                    Log.d("MILLIS", System.currentTimeMillis() + "");
                    setupRecyclerView(monumentEntities);
                });
    }
    private void setupRecyclerView(List<MonumentEntity> monuments){
        RecyclerView recyclerView = (RecyclerView)dialogPlus.getHolderView().findViewById(R.id.recyclerView);
        MonumentEntityAdapter adapter = new MonumentEntityAdapter(monuments);
        adapter.setOnPlaceClickListener((monument, index) -> {
            dialogPlus.dismiss();
            onMarkerClick(Observable.fromIterable(markers).filter(marker -> marker.getTitle().equals("Marker" + monument.getId())).blockingFirst(), map);
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    @Override
    public void showSearch(List<MonumentEntity> monuments) {


    }

    @Override
    public boolean onMarkerClick(Marker marker, org.osmdroid.views.MapView mapView) {
        for (Marker marker1 : markers){
            if (marker1.isInfoWindowShown()) marker1.closeInfoWindow();
        }
        marker.showInfoWindow();
        map.getController().setCenter(marker.getPosition());
        return true;
    }

    private void initRadioGroup(){
        radioGroup = ((RadioGroup)dialogPlus.getHolderView().findViewById(R.id.group));
        for (int i = 0; i <= radioButtonTitles.length - 1; i++){
            RadioButton rb = new RadioButton(getContext());
            rb.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            rb.setText(radioButtonTitles[i]);
            rb.setId(1000 + i);
            radioGroup.addView(rb, i);
        }
        radioGroup.check(1000 + 0);
        radioGroup.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case 1000:
                setupRecyclerView(monuments);
                //showMonuments(monuments, getPresenter().place.getImgRoot(), false);
                break;
            case 1001:
                setupRecyclerView(Observable.fromIterable(monuments).filter(monumentEntity -> monumentEntity.getType().equals("1")).toList().blockingGet());
                break;
            case 1002:
                setupRecyclerView(Observable.fromIterable(monuments).filter(monumentEntity -> monumentEntity.getType().equals("2")).toList().blockingGet());
                break;
            default:
                break;
        }
    }

    public static MapFragment newInstance(int placeId, double lat, double lng) {
        Bundle bundle = new Bundle();
        bundle.putInt("place_id", placeId);
        bundle.putDouble("lat", lat);
        bundle.putDouble("lng", lng);
        MapFragment myFragment = new MapFragment();
        myFragment.setArguments(bundle);
        return myFragment;
    }
}
