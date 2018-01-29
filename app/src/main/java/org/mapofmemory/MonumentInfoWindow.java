package org.mapofmemory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.screens.monument.MonumentActivity;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.InfoWindow;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by The Tronuo on 28.01.2018.
 */

public class MonumentInfoWindow extends InfoWindow {
    @BindView(R.id.image) ImageView image;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.type) TextView type;
    private String imageUrl = "";
    private MonumentEntity monument;
    public MonumentInfoWindow(MapView mapView, String imageUrl, MonumentEntity monument) {
        super(R.layout.window_monument, mapView);
        this.imageUrl = imageUrl;
        this.monument = monument;
    }

    public ImageView getImage() {
        return image;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public void onOpen(Object item) {
        ButterKnife.bind(this, getView());
        Log.d("IMAGE URL", imageUrl);
        if (!imageUrl.isEmpty()) Picasso.with(image.getContext()).load(imageUrl).error(R.drawable.no_photo).into(image);
        title.setText(monument.getName());
        type.setText(monument.getType2());
        getView().setOnClickListener(v -> onWindowClickListener.onClick(this));
    }

    public interface OnWindowClickListener{
        void onClick(MonumentInfoWindow window);
    }
    private OnWindowClickListener onWindowClickListener;

    public void setOnWindowClickListener(OnWindowClickListener onWindowClickListener) {
        this.onWindowClickListener = onWindowClickListener;
    }

    @Override
    public void onClose() {

    }
}
