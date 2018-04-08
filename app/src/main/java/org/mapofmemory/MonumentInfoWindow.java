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
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by The Tronuo on 28.01.2018.
 */

public class MonumentInfoWindow extends InfoWindow {
    @BindView(R.id.image) ImageView image;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.type) TextView type;
    @BindView(R.id.btn_route) FancyButton fancyButton;
    private boolean fancyButtonGone = false;
    private String imageUrl = "";
    private MonumentEntity monument;
    public MonumentInfoWindow(MapView mapView, String imageUrl, MonumentEntity monument) {
        super(R.layout.window_monument, mapView);
        this.imageUrl = imageUrl;
        this.monument = monument;
    }

    public void hideBtn(){
        try {
            fancyButton.setVisibility(View.GONE);
        }
        catch (Exception e){
            fancyButtonGone = true;

        }
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
        if (fancyButtonGone) fancyButton.setVisibility(View.GONE);
        if (!imageUrl.isEmpty()) Picasso.with(image.getContext()).load(imageUrl).error(R.drawable.no_photo).into(image);
        title.setText(monument.getName());
        type.setText(monument.getType2());
        getView().setOnClickListener(v -> onWindowClickListener.onWindowClick(this));
        fancyButton.setOnClickListener(v -> onWindowClickListener.onButtonClick(this));
    }

    public interface OnWindowClickListener{
        void onWindowClick(MonumentInfoWindow window);
        void onButtonClick(MonumentInfoWindow window);
    }
    private OnWindowClickListener onWindowClickListener;

    public void setOnWindowClickListener(OnWindowClickListener onWindowClickListener) {
        this.onWindowClickListener = onWindowClickListener;
    }

    @Override
    public void onClose() {

    }
}
