package org.mapofmemory.screens.map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.mapofmemory.MonumentInfoWindow;
import org.mapofmemory.R;
import org.mapofmemory.adapters.MonumentEntityAdapter;
import org.mapofmemory.adapters.SearchAdapter;
import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.screens.main.MainActivity;
import org.mapofmemory.screens.monument.MonumentActivity;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.DefaultOverlayManager;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.TilesOverlay;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by The Tronuo on 27.01.2018.
 */

public class MapFragment extends MvpFragment<MapView, MapPresenter> implements MapView, Marker.OnMarkerClickListener, SmartTabLayout.OnTabClickListener {
    @BindView(R.id.map) org.osmdroid.views.MapView map;
    @BindView(R.id.progress) ProgressBar progressBar;
    @BindDrawable(R.drawable.ic_blue_marker) Drawable blueMarker;
    @BindDrawable(R.drawable.ic_red_marker) Drawable redMarker;
    @BindView(R.id.viewpagertab) SmartTabLayout smartTabLayout;
    @BindView(R.id.viewpager) ViewPager viewPager;

    private DialogPlus dialogPlus;
    private Activity activity;
    private List<MonumentEntity> monuments;
    private MaterialSearchView searchView;
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
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getController().setZoom(16);
        map.setMaxZoomLevel(19);
        map.setMultiTouchControls(true);
        GeoPoint startPoint = new GeoPoint(getArguments().getDouble("lat"), getArguments().getDouble("lng"));
        map.getController().setCenter(startPoint);
        int statusBarHeight = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        }
        dialogPlus = DialogPlus.newDialog(getActivity())
                .setContentHolder(new ViewHolder(R.layout.view_search))
                .setExpanded(true, (int)(1.0f * getActivity().getWindowManager().getDefaultDisplay().getHeight()) - statusBarHeight)
                .create();
        getPresenter().loadMonuments();

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getActivity().getSupportFragmentManager(), FragmentPagerItems.with(getActivity())
                .add("Все", Fragment.class)
                .add("Личные", Fragment.class)
                .add("Коллективные", Fragment.class)
                .create());
        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);
        smartTabLayout.setOnTabClickListener(this);
    }

    List<Marker> markers = new ArrayList<>();

    @Override
    public void onTabClicked(int position) {
        if (position == 0){
            showMonuments(getPresenter().getMonuments(), getPresenter().place.getImgRoot());
        }
        else if (position == 1){
            showMonuments(Observable.fromIterable(getPresenter().getMonuments())
                    .filter(monumentEntity -> monumentEntity.getType().equals("1"))
                    .toList()
                    .blockingGet(), getPresenter().place.getImgRoot());
        }
        else if (position == 2){
            showMonuments(Observable.fromIterable(getPresenter().getMonuments())
                    .filter(monumentEntity -> monumentEntity.getType().equals("2"))
                    .toList()
                    .blockingGet(), getPresenter().place.getImgRoot());
        }
    }

    @Override
    public void showMonuments(List<MonumentEntity> monuments, String imgRoot) {
        //smartTabLayout.setVisibility(View.GONE);
        markers.clear();
        map.getOverlays().clear();
        map.getOverlayManager().clear();
        this.monuments = monuments;
        List<String> suggestions = Observable.fromIterable(monuments)
                .filter(monumentEntity -> !monumentEntity.getRealName().isEmpty())
                .map(monumentEntity -> monumentEntity.getRealName())
                .toList()
                .blockingGet();
        ((MainActivity)activity).searchView.setAdapter(new SearchAdapter(activity, suggestions.toArray(new String[suggestions.size()])));
        ((MainActivity)activity).searchView.setOnItemClickListener(((parent, view, position, id) -> {
            TextView suggestion = (TextView) view.findViewById(R.id.suggestion_text);
            MonumentEntity monument = Observable.fromIterable(getPresenter().getMonuments())
                    .filter(monumentEntity -> monumentEntity.getRealName().equals(suggestion.getText().toString()))
                    .blockingFirst();
            ((MainActivity)activity).searchView.dismissSuggestions();
            ((MainActivity)activity).searchView.hideKeyboard(((MainActivity)activity).searchView);
            Observable.just(1)
                    .delay(200, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(res ->{
                        onMarkerClick(Observable.fromIterable(markers).filter(marker -> marker.getTitle().equals("Marker" + monument.getId())).blockingFirst(), map);
                    });
        }));
        map.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Flowable.fromIterable(monuments)
                .subscribeOn(Schedulers.io())
                .filter(monumentEntity ->{
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
                            newInt.putExtra("monument_id", monumentEntity.getNum() + "");
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
                    return true;
                })
                .toList()
                .filter(v -> {map.getOverlays().addAll(markers); return true;})
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(monumentEntities -> {
                    progressBar.setVisibility(View.GONE);
                    map.invalidate();
                    map.setVisibility(View.VISIBLE);
                    smartTabLayout.setVisibility(View.VISIBLE);
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
        map.getController().setZoom(18);
        map.getController().setCenter(new GeoPoint(marker.getPosition().getLatitude(), marker.getPosition().getLongitude()));
        map.invalidate();
        return true;
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
