package org.mapofmemory.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.mapofmemory.R;
import org.mapofmemory.entities.PlaceEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by The Tronuo on 21.01.2018.
 */

public class PlaceEntityAdapter extends RecyclerView.Adapter<PlaceEntityAdapter.PlaceHolder> {
    private List<PlaceEntity> places;

    public class PlaceHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.title) TextView titleView;
        @BindView(R.id.descr) TextView descrView;
        @BindView(R.id.image) RoundedImageView imageView;
        @BindView(R.id.block) LinearLayout block;
        private Context context;
        public PlaceHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            this.context = v.getContext();
        }

        public Context getContext(){
            return context;
        }
    }

    private OnPlaceClickListener onPlaceClickListener;
    public interface OnPlaceClickListener{
        void onClick(PlaceEntity place);
    }

    public void setOnPlaceClickListener(OnPlaceClickListener onPlaceClickListener) {
        this.onPlaceClickListener = onPlaceClickListener;
    }

    public PlaceEntityAdapter(List<PlaceEntity> places) {
        this.places = places;
    }

    @Override
    public PlaceHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place, parent, false);
        PlaceHolder vh = new PlaceHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(PlaceHolder holder, int position) {
        PlaceEntity place = places.get(position);
        Log.w("log", "onBindViewHolder: " + place.getImgRoot() + place.getImg() );

     //   Picasso.with(holder.getContext()).load(place.getImgRoot() + place.getImg()).into(holder.imageView);

        Glide.with(holder.getContext()).load(place.getImgRoot() + place.getImg()).into(holder.imageView);

        holder.titleView.setText(place.getTitle());
        holder.descrView.setText(place.getDescr());
        holder.block.setOnClickListener(v -> onPlaceClickListener.onClick(place));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }
}
