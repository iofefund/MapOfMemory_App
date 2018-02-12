package org.mapofmemory.screens.monument;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.transition.Fade;
import android.text.Html;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.squareup.picasso.Picasso;

import org.mapofmemory.R;
import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.screens.navigator.NavigatorActivity;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MonumentActivity extends MvpActivity<MonumentView, MonumentPresenter> implements MonumentView {
    @BindView(R.id.image) ImageView image;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.text) TextView text;
    @BindView(R.id.name) TextView name;
    @BindView(R.id.type) TextView type;
    @BindView(R.id.toolbar_layout) CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.map_view) MapView mapView;
    @BindDrawable(R.drawable.ic_blue_marker) Drawable blueMarker;
    @BindDrawable(R.drawable.ic_red_marker) Drawable redMarker;

    @OnClick(R.id.btn_route) void onRoute(){
        Intent newInt = new Intent(this, NavigatorActivity.class);
        newInt.putExtra("monument_id", getIntent().getStringExtra("monument_id"));
        startActivity(newInt);
    }

    @NonNull
    @Override
    public MonumentPresenter createPresenter() {
        return new MonumentPresenter(this, getIntent().getStringExtra("monument_id"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadMonument(MonumentEntity monumentEntity) {
        if (!getIntent().hasExtra("name")) {
            text.setText(Html.fromHtml(monumentEntity.getDesc()));
            name.setText(monumentEntity.getName());
            if (!monumentEntity.getType2().isEmpty()) {
                type.setText(monumentEntity.getType2());
            } else {
                type.setVisibility(View.GONE);
            }
            toolbarLayout.setTitle("Назад к именам");
        }
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMaxZoomLevel(19);
        mapView.setMultiTouchControls(false);
        try {
            final GeoPoint startPoint = new GeoPoint(Float.parseFloat(monumentEntity.getLat()), Float.parseFloat(monumentEntity.getLng()));
            Marker startMarker = new Marker(mapView);
            startMarker.setPosition(startPoint);
            startMarker.setAnchor(Marker.ANCHOR_CENTER, 1.0f);
            startMarker.setIcon(monumentEntity.getType().equals("1") ? redMarker : blueMarker);
            mapView.getOverlays().add(startMarker);
            mapView.getController().setZoom(15);
            mapView.getController().setCenter(startPoint);
            mapView.invalidate();
            mapView.setVisibility(View.VISIBLE);
            mapView.setOnTouchListener((View arg0, MotionEvent arg1) -> {
                        if (arg1.getAction() == MotionEvent.ACTION_UP) {
                            mapView.getController().setCenter(startPoint);
                            return true;
                        }
                        if (arg1.getPointerCount() > 1) {
                            mapView.getController().setCenter(startPoint);
                            return false;
                        } else {
                            return true;
                        }
                    }
            );
        }
        catch (Exception e){

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monument);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Picasso.with(this).load(getIntent().getStringExtra("image_url")).into(image);
        if (getIntent().hasExtra("name")) {
            text.setText(Html.fromHtml(getIntent().getStringExtra("descr")));
            if (!getIntent().getStringExtra("type2").isEmpty()) {
                type.setText(getIntent().getStringExtra("type2"));
            } else {
                type.setVisibility(View.GONE);
            }
            toolbarLayout.setTitle("Назад к карте");
            name.setText(getIntent().getStringExtra("name"));
        }
        getPresenter().loadMonument();
    }

}
