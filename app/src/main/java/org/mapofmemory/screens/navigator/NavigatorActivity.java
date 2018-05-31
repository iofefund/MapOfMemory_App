package org.mapofmemory.screens.navigator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import org.mapofmemory.MonumentInfoWindow;
import org.mapofmemory.R;
import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.screens.main.MainActivity;
import org.mapofmemory.screens.monument.MonumentActivity;
import org.mapofmemory.screens.monument.MonumentView;
import org.osmdroid.bonuspack.routing.OSRMRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationAccuracy;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NavigatorActivity extends MvpActivity<NavigatorView, NavigatorPresenter> implements SensorEventListener, NavigatorView, OnLocationUpdatedListener, Marker.OnMarkerClickListener{
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.map_view) MapView mapView;
    @BindDrawable(R.drawable.ic_blue_marker) Drawable blueMarker;
    @BindDrawable(R.drawable.ic_red_marker) Drawable redMarker;
    @BindView(R.id.distance) TextView distance;
    @BindView(R.id.block) RelativeLayout distanceBlock;
    private boolean isPermission = false;

    @NonNull
    @Override
    public NavigatorPresenter createPresenter() {
        return new NavigatorPresenter(this, getIntent().getStringExtra("monument_id"));
    }

    @Override
    public void onBackPressed() {
        SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.unregisterListener(this);
        SmartLocation.with(NavigatorActivity.this).location().stop();
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    private long millis = 0;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float[] gravity = event.values.clone();
            if (System.currentTimeMillis() - millis >= 50){
                millis = System.currentTimeMillis();
                if (userMarker != null){
                    userMarker.setRotation(gravity[0]);
                }
                mapView.invalidate();
                Log.d("Heading", gravity[0] + "");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigator);
        ButterKnife.bind(this);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getPresenter().loadMonument();
        if (getIntent().hasExtra("from_map")){
            getSupportActionBar().setTitle("Назад к карте");
        }
        else{
            getSupportActionBar().setTitle("Назад к описанию");
        }
        SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        Sensor accSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);


        mSensorManager.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onLoadMonument(MonumentEntity monumentEntity, String imgRoot) {
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMaxZoomLevel(19);
        mapView.setMultiTouchControls(true);
        final GeoPoint startPoint = new GeoPoint(Float.parseFloat(monumentEntity.getLat()), Float.parseFloat(monumentEntity.getLng()));
        monumentMarker = new Marker(mapView);
        monumentMarker.setPosition(startPoint);
        monumentMarker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
        monumentMarker.setIcon(monumentEntity.getType().equals("1") ? redMarker : blueMarker);
        MonumentInfoWindow monumentInfoWindow = new MonumentInfoWindow(mapView, getPresenter().getMonument().getImgs().size() != 0 ? imgRoot + getPresenter().getMonument().getImgs().get(0).getImg() : "", getPresenter().getMonument());
        monumentInfoWindow.hideBtn();
        monumentInfoWindow.setOnWindowClickListener(new MonumentInfoWindow.OnWindowClickListener() {
            @Override
            public void onWindowClick(MonumentInfoWindow window) {
                ImageView image = window.getImage();
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeClipRevealAnimation(image, (int) image.getX(), (int) image.getY(), image.getWidth(), image.getHeight());
                Intent newInt = new Intent(NavigatorActivity.this, MonumentActivity.class);
                newInt.putExtra("monument_id", monumentEntity.getNum() + "");
                newInt.putExtra("image_url", window.getImageUrl());
                newInt.putExtra("name", monumentEntity.getName());
                newInt.putExtra("type2", monumentEntity.getType2());
                newInt.putExtra("descr", monumentEntity.getDesc());
                startActivity(newInt, options.toBundle());
            }

            @Override
            public void onButtonClick(MonumentInfoWindow window) {

            }

            @Override
            public void onCloseClick() {
                monumentInfoWindow.close();
            }
        });


        monumentMarker.setInfoWindow(monumentInfoWindow);
        mapView.getOverlays().add(monumentMarker);
        mapView.getController().setZoom(19);
        mapView.getController().setCenter(startPoint);
        mapView.invalidate();
        mapView.setVisibility(View.VISIBLE);
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        SmartLocation.with(getApplicationContext()).location().config((new LocationParams.Builder()).setAccuracy(LocationAccuracy.HIGH).setDistance(0.0F).setInterval(1000L).build())
                                .start(NavigatorActivity.this);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
    }

    @Override
    public void onLocationUpdated(Location location) {
        mapView.getOverlays().clear();
        updateMap(location.getLatitude(), location.getLongitude());
        //updateMap(62.8878098, 34.6796229);
        //updateMap(59.9171483,30.0448854);
    }

    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        if (marker.isInfoWindowOpen()){
            marker.closeInfoWindow();
        }
        else{
            marker.showInfoWindow();
        }
        mapView.getController().setZoom(19);
        mapView.getController().setCenter(new GeoPoint(marker.getPosition().getLatitude(), marker.getPosition().getLongitude()));
        mapView.invalidate();
        return true;
    }
    private Marker userMarker = null;
    private Marker monumentMarker = null;
    private int i = 0;
    private Polyline line = null;
    private void updateMap(double userLat, double userLng){
        i++;
        userMarker = null;
        final GeoPoint endPoint = new GeoPoint(Float.parseFloat(getPresenter().getMonument().getLat()), Float.parseFloat(getPresenter().getMonument().getLng()));
        Location loc1 = new Location("");
        /*if (i == 1) {
            monumentMarker = new Marker(mapView);
            monumentMarker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
            monumentMarker.setIcon(getPresenter().getMonument().getType().equals("1") ? redMarker : blueMarker);
            monumentMarker.setOnMarkerClickListener(this);
            MonumentInfoWindow monumentInfoWindow = new MonumentInfoWindow(mapView, getPresenter().getMonument().getImgs().size() != 0 ? getPresenter().getPlace().getImgRoot() + getPresenter().getMonument().getImgs().get(0).getImg() : "", getPresenter().getMonument());
            monumentInfoWindow.setOnWindowClickListener(new MonumentInfoWindow.OnWindowClickListener() {
                @Override
                public void onWindowClick(MonumentInfoWindow window) {
                    onMarkerClick(monumentMarker, mapView);
                }

                @Override
                public void onButtonClick(MonumentInfoWindow window) {

                }
            });
            monumentMarker.setInfoWindow(monumentInfoWindow);
            //monumentMarker.showInfoWindow();
            //monumentInfoWindow.hideBtn();
            mapView.getOverlays().add(monumentMarker);
        }
        monumentMarker.setPosition(endPoint);*/

        boolean b = false;
        if (userMarker == null){
            b = true;
            userMarker = new Marker(mapView);
        }
        userMarker.setPosition(new GeoPoint(userLat, userLng));
        userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        userMarker.setInfoWindow(null);
        userMarker.setIcon(getDrawable(R.drawable.point));
        mapView.getController().setCenter(userMarker.getPosition());
        loc1.setLatitude(userLat);
        loc1.setLongitude(userLng);
        Location loc2 = new Location("");
        loc2.setLatitude(Double.parseDouble(getPresenter().getMonument().getLat()));
        loc2.setLongitude(Double.parseDouble(getPresenter().getMonument().getLng()));
        userMarker.setRotation(loc1.bearingTo(loc2));
        int distanceTo = (int)loc1.distanceTo(loc2);
        distance.setText(distanceTo + " М");
        distanceBlock.setVisibility(View.VISIBLE);


        if (i == 1){
            double maxLat = Math.max(userMarker.getPosition().getLatitude(), monumentMarker.getPosition().getLatitude());
            double maxLng = Math.max(userMarker.getPosition().getLongitude(), monumentMarker.getPosition().getLongitude());
            double minLat = Math.min(userMarker.getPosition().getLatitude(), monumentMarker.getPosition().getLatitude());
            double minLng = Math.min(userMarker.getPosition().getLongitude(), monumentMarker.getPosition().getLongitude());
            mapView.zoomToBoundingBox(new BoundingBox(maxLat, maxLng, minLat, minLng), true);
        }
        if (line == null){
            line = new Polyline(getApplicationContext());
            line.setWidth(8f);
            line.setColor(Color.BLUE);
        }
        List<GeoPoint> pts = new ArrayList<>();
        pts.add(new GeoPoint(userLat, userLng));
        pts.add(new GeoPoint(Double.parseDouble(getPresenter().getMonument().getLat()), Double.parseDouble(getPresenter().getMonument().getLng())));
        line.setPoints(pts);
        if (!mapView.getOverlays().contains(line)) mapView.getOverlays().add(line);
        if (!mapView.getOverlays().contains(userMarker)) mapView.getOverlays().add(userMarker);
        if (!mapView.getOverlays().contains(monumentMarker)) mapView.getOverlays().add(monumentMarker);

        //mapView.invalidate();
        /*int distanceTo = (int)loc1.distanceTo(loc2);
        if (distanceTo >= 2000){
            Observable.just(1)
                    .subscribeOn(Schedulers.io())
                    .map(res -> {
                        RoadManager roadManager = new OSRMRoadManager(getApplicationContext());
                        ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>();
                        waypoints.add(new GeoPoint(userLat, userLng));
                        waypoints.add(endPoint);
                        Road road = roadManager.getRoad(waypoints);
                        Polyline roadOverlay = RoadManager.buildRoadOverlay(road);
                        return roadOverlay;
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(roadOverlay -> {
                        mapView.getOverlays().add(roadOverlay);
                        mapView.invalidate();
                        distance.setText(distanceTo + " м");
                        distanceBlock.setVisibility(View.VISIBLE);
                    });
        }
        else{
            Polyline line = new Polyline(getApplicationContext());
            line.setWidth(8f);
            line.setColor(Color.RED);
            List<GeoPoint> pts = new ArrayList<>();
            pts.add(new GeoPoint(userLat, userLng));
            pts.add(new GeoPoint(Double.parseDouble(getPresenter().getMonument().getLat()), Double.parseDouble(getPresenter().getMonument().getLng())));
            line.setPoints(pts);
            mapView.getOverlayManager().add(line);
            mapView.invalidate();
            distance.setText(distanceTo + " м");
            distanceBlock.setVisibility(View.VISIBLE);
        }*/
    }


}
