package org.mapofmemory.screens.map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.mapofmemory.AppConfig;
import org.mapofmemory.MonumentInfoWindow;
import org.mapofmemory.R;
import org.mapofmemory.adapters.CustomSuggestionsAdapter;
import org.mapofmemory.adapters.SearchAdapter;
import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.screens.main.MainActivity;
import org.mapofmemory.screens.monument.MonumentActivity;
import org.mapofmemory.screens.navigator.NavigatorActivity;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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
    @BindView(R.id.searchBar) MaterialSearchBar searchBar;

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
        ((ImageView)searchBar.findViewById(R.id.mt_nav)).setImageResource(R.drawable.ic_search);
        progressBar.setVisibility(View.VISIBLE);
        this.activity = getActivity();
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.getController().setZoom(18);
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
    private int pos = 0;

    @Override
    public void onTabClicked(int position) {
        pos = position;
        if (searchBar.isSearchEnabled()) {
            searchBar.disableSearch();
        }
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

    private Disposable disp = null;

    @Override
    public void showMonuments(List<MonumentEntity> monuments, String imgRoot) {
        //smartTabLayout.setVisibility(View.GONE);
        for (Marker marker1 : markers){
            if (marker1.isInfoWindowShown()) marker1.closeInfoWindow();
        }
        markers.clear();
        map.getOverlays().clear();
        map.getOverlayManager().clear();
        this.monuments = monuments;
        initSearchView();
        map.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        disp = Flowable.fromIterable(monuments)
                .subscribeOn(Schedulers.io())
                .filter(monumentEntity ->{
                        GeoPoint startPoint = new GeoPoint(Double.parseDouble(monumentEntity.getLat()), Double.parseDouble(monumentEntity.getLng()));
                        Marker startMarker = new Marker(map);
                        startMarker.setOnMarkerClickListener(this);
                        startMarker.setPosition(startPoint);
                        startMarker.setTitle("Marker" + monumentEntity.getId());
                        MonumentInfoWindow monumentInfoWindow = new MonumentInfoWindow(map, monumentEntity.getImgs().size() != 0 ? imgRoot + monumentEntity.getImgs().get(0).getImg() : "", monumentEntity);
                        monumentInfoWindow.setOnWindowClickListener(new MonumentInfoWindow.OnWindowClickListener() {
                            @Override
                            public void onWindowClick(MonumentInfoWindow window) {
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
                            }

                            @Override
                            public void onButtonClick(MonumentInfoWindow window) {
                                Intent newInt = new Intent(getContext(), NavigatorActivity.class);
                                newInt.putExtra("monument_id", monumentEntity.getNum() + "");
                                newInt.putExtra("from_map", "");
                                startActivity(newInt);
                            }
                        });
                        startMarker.setInfoWindow(monumentInfoWindow);
                        startMarker.setAnchor(Marker.ANCHOR_BOTTOM, 1.0f);
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
                    searchBar.setVisibility(View.VISIBLE);
                    smartTabLayout.setVisibility(View.VISIBLE);
                });
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

    public void initSearchView(){
        List<String> suggestions = Observable.fromIterable(monuments)
                .filter(monumentEntity -> !monumentEntity.getKeywords().isEmpty() || !monumentEntity.getName().isEmpty())
                .map(monumentEntity -> monumentEntity.getType().equals("1") ? monumentEntity.getKeywords() : monumentEntity.getName())
                .toList()
                .blockingGet();
        suggestions = AppConfig.removeTheDuplicates(suggestions);
        final List<String> suggs = suggestions;
        CustomSuggestionsAdapter customSuggestionsAdapter = new CustomSuggestionsAdapter(getLayoutInflater());
        customSuggestionsAdapter.setOnSuggestionClickListener(new CustomSuggestionsAdapter.OnSuggestionClickListener() {
            @Override
            public void onClick(String text) {
                List<MonumentEntity> m = Observable.fromIterable(getPresenter().getMonuments())
                        .filter(monumentEntity -> {
                            return monumentEntity.getType().equals("1") ? monumentEntity.getKeywords().toLowerCase().contains(text.toString().toLowerCase()) : monumentEntity.getName().toLowerCase().contains(text.toString().toLowerCase());
                        })
                        .toList()
                        .blockingGet();
                searchBar.hideSuggestionsList();
                InputMethodManager imm =(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
                //searchBar.
                Disposable ob = Observable.just(1)
                        .delay(300, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(res ->{
                            showMonuments(m, getPresenter().place.getImgRoot());
                            if (m.size() == 1){
                                map.getController().setCenter(new GeoPoint(Float.parseFloat(m.get(0).getLat()), Float.parseFloat(m.get(0).getLng())));
                                map.invalidate();
                            }
                            searchBar.setText(text);
                            //searchBar.disableSearch();
                            /*onMarkerClick(Observable.fromIterable(markers)
                                    .filter(marker -> marker.getTitle().equals("Marker" + m.get(0).getId()))
                                    .blockingFirst(), map);*/
                        });
            }
        });

        customSuggestionsAdapter.setSuggestions(suggestions);
        searchBar.setCustomSuggestionAdapter(customSuggestionsAdapter);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customSuggestionsAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        searchBar.findViewById(R.id.mt_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTabClicked(0);

            }
        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled){
                    ((ImageView)searchBar.findViewById(R.id.mt_nav)).setImageResource(R.drawable.ic_search);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                List<MonumentEntity> m = Observable.fromIterable(getPresenter().getMonuments())
                        .filter(monumentEntity -> {
                            return monumentEntity.getType().equals("1") ? monumentEntity.getKeywords().toLowerCase().contains(text.toString().toLowerCase()) : monumentEntity.getName().toLowerCase().contains(text.toString().toLowerCase());
                        })
                        .toList()
                        .blockingGet();
                searchBar.hideSuggestionsList();
                InputMethodManager imm =(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
                //searchBar.
                Disposable ob = Observable.just(1)
                        .delay(100, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(res ->{
                            showMonuments(m, getPresenter().place.getImgRoot());
                            if (m.size() == 1){
                                map.getController().setCenter(new GeoPoint(Float.parseFloat(m.get(0).getLat()), Float.parseFloat(m.get(0).getLng())));
                                map.invalidate();
                            }
                            /*onMarkerClick(Observable.fromIterable(markers)
                                    .filter(marker -> marker.getTitle().equals("Marker" + m.get(0).getId()))
                                    .blockingFirst(), map);*/
                        });
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if (buttonCode == MaterialSearchBar.BUTTON_BACK){
                    onTabClicked(pos);
                }
                Toast.makeText(getActivity(), buttonCode + "!", Toast.LENGTH_LONG).show();
            }
        });
        //searchBar.setLastSuggestions(suggestions);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPresenter().dispose();
        if (disp != null) disp.dispose();
    }
}
