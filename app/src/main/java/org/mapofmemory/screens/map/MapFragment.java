package org.mapofmemory.screens.map;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;

import org.mapofmemory.R;
import org.mapofmemory.entities.MonumentEntity;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by The Tronuo on 27.01.2018.
 */

public class MapFragment extends MvpFragment<MapView, MapPresenter> implements MapView {
    @BindView(R.id.map) org.osmdroid.views.MapView map;


    @Override
    public MapPresenter createPresenter() {
        return new MapPresenter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        IMapController mapController = map.getController();
        mapController.setZoom(30);
        map.setMaxZoomLevel(40);
        GeoPoint startPoint = new GeoPoint(getArguments().getDouble("lat"), getArguments().getDouble("lng"));
        mapController.setCenter(startPoint);

        getPresenter().loadMonuments();
    }

    @Override
    public void showMonuments(List<MonumentEntity> monuments) {
        for (MonumentEntity monument : monuments){
            try {
                GeoPoint startPoint = new GeoPoint(Double.parseDouble(monument.getLat()), Double.parseDouble(monument.getLng()));
                Marker startMarker = new Marker(map);
                startMarker.setPosition(startPoint);
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                startMarker.setIcon(getResources().getDrawable(R.drawable.ic_marker));
                map.getOverlays().add(startMarker);
            }
            catch (Exception e){

            }
        }

    }

    public static MapFragment newInstance(double lat, double lng) {
        Bundle bundle = new Bundle();
        bundle.putDouble("lat", lat);
        bundle.putDouble("lng", lng);
        MapFragment myFragment = new MapFragment();
        myFragment.setArguments(bundle);
        return myFragment;
    }
}
