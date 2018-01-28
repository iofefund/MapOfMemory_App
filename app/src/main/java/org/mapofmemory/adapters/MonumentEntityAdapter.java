package org.mapofmemory.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.mapofmemory.R;
import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.entities.PlaceEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by The Tronuo on 21.01.2018.
 */

public class MonumentEntityAdapter extends RecyclerView.Adapter<MonumentEntityAdapter.MonumentHolder> {
    private List<MonumentEntity> monuments;

    public class MonumentHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.title) TextView titleView;
        @BindView(R.id.type) TextView typeView;
        private Context context;
        public MonumentHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            this.context = v.getContext();
        }

        public Context getContext(){
            return context;
        }
    }

    private OnMonumentClickListener onMonumentClickListener;
    public interface OnMonumentClickListener{
        void onClick(MonumentEntity monument, int i);
    }

    public void setOnPlaceClickListener(OnMonumentClickListener onMonumentClickListener) {
        this.onMonumentClickListener = onMonumentClickListener;
    }

    public MonumentEntityAdapter(List<MonumentEntity> monuments) {
        this.monuments = monuments;
    }

    @Override
    public MonumentHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_monument, parent, false);
        MonumentHolder vh = new MonumentHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MonumentHolder holder, int position) {
        holder.typeView.setVisibility(View.VISIBLE);
        MonumentEntity monument = monuments.get(position);
        holder.titleView.setText(monument.getName().trim());
        holder.typeView.setText(monument.getType2().trim());
        if (holder.typeView.getText().toString().isEmpty()) holder.typeView.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(v -> onMonumentClickListener.onClick(monument, position));
    }

    @Override
    public int getItemCount() {
        return monuments.size();
    }
}
