package org.mapofmemory.screens.route;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;

import org.mapofmemory.R;
import org.mapofmemory.SimpleDividerItemDecoration;
import org.mapofmemory.adapters.RouteAdapter;
import org.mapofmemory.entities.PlaceEntity;
import org.mapofmemory.entities.RouteEntity;
import org.mapofmemory.entities.RouteInfo;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by The Tronuo on 04.02.2018.
 */

public class RouteFragment extends MvpFragment<RouteView, RoutePresenter> implements RouteView{
    @BindView(R.id.destination) TextView destination;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.map_view) MapView mapView;
    @BindView(R.id.progress) ProgressBar progressBar;
    @BindView(R.id.nested) NestedScrollView nestedScrollView;

    @Override
    public RoutePresenter createPresenter() {
        return new RoutePresenter(getActivity(), getArguments().getInt("place_id", -1));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        getPresenter().loadRoute();

    }
    public GeoPoint midPoint (double lat1, double lon1, double lat2, double lon2){

        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);
        return new GeoPoint(Math.toDegrees(lat3), Math.toDegrees(lon3));
    }

    @Override
    public void onPlaceLoaded(PlaceEntity place, RouteEntity route) {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMaxZoomLevel(20);
        //mapView.setUseDataConnection(false);
        mapView.setMultiTouchControls(false);
        GeoPoint startPoint = null;
        if (route.getStartLat().equals("0") && route.getStartLng().equals("0")){
            startPoint = new GeoPoint(place.getLat(), place.getLng());
            Marker startMarker = new Marker(mapView);
            startMarker.setPosition(startPoint);
            startMarker.setAnchor(Marker.ANCHOR_RIGHT, 1.0f);
            startMarker.setIcon(getResources().getDrawable(R.drawable.ic_blue_marker));
            mapView.getOverlays().add(startMarker);
            mapView.getController().setZoom(16);
            mapView.getController().setCenter(startPoint);
            mapView.invalidate();
        }
        else{
            mapView.getController().setZoom(12);
            mapView.setFlingEnabled(false);
            startPoint = midPoint(Float.parseFloat(route.getStartLat()), Float.parseFloat(route.getStartLng()), place.getLat(), place.getLng());
            mapView.getController().setCenter(startPoint);
            Observable.just(1)
                    .subscribeOn(Schedulers.io())
                    .map(res -> {
                        RoadManager roadManager = new OSRMRoadManager(getActivity());
                        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
                        waypoints.add(new GeoPoint(Float.parseFloat(route.getStartLat()), Float.parseFloat(route.getStartLng())));
                        GeoPoint endPoint = new GeoPoint(place.getLat(), place.getLng());
                        waypoints.add(endPoint);
                        Road road = roadManager.getRoad(waypoints);
                        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
                        return roadOverlay;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(roadOverlay -> {
                        mapView.getOverlays().add(roadOverlay);
                        mapView.invalidate();
                    });
        }
        final GeoPoint gp = startPoint;
        mapView.setOnTouchListener((View arg0, MotionEvent arg1) -> {
                    if(arg1.getAction() ==  MotionEvent.ACTION_UP)
                    {
                        mapView.getController().setCenter(gp);
                        try {
                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + place.getLat() + "," + place.getLng());
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);
                        }
                        catch (Exception e){

                        }
                        return true;
                    }
                    if(arg1.getPointerCount() > 1)
                    {
                        mapView.getController().setCenter(gp);
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }
        );
        mapView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        nestedScrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRouteFailure() {
    }

    @Override
    public void onRouteLoaded(RouteEntity routeEntity) {
        destination.setText(Html.fromHtml(routeEntity.getAddress()));
        List<RouteInfo> infos = new ArrayList<>();
        if (!routeEntity.getBusRoute().isEmpty()){
            RouteInfo routeInfo = new RouteInfo();
            routeInfo.setHeader("Как добраться на автобусе");
            routeInfo.setMessage(routeEntity.getBusRoute());
            routeInfo.setIcon(getResources().getDrawable(R.drawable.ic_bus));
            infos.add(routeInfo);
        }
        if (!routeEntity.getCarRoute().isEmpty()){
            RouteInfo routeInfo = new RouteInfo();
            routeInfo.setHeader("Как добраться на машине");
            routeInfo.setMessage(routeEntity.getCarRoute());
            routeInfo.setIcon(getResources().getDrawable(R.drawable.ic_car));
            infos.add(routeInfo);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        RouteAdapter routeAdapter = new RouteAdapter(infos);
        recyclerView.setAdapter(routeAdapter);
    }

    public static RouteFragment newInstance(int placeId) {
        Bundle bundle = new Bundle();
        bundle.putInt("place_id", placeId);
        RouteFragment myFragment = new RouteFragment();
        myFragment.setArguments(bundle);
        return myFragment;
    }
}
