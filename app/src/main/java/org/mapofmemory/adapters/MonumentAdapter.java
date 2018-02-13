package org.mapofmemory.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import com.squareup.picasso.Picasso;

import org.mapofmemory.R;
import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.entities.PersonEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by The Tronuo on 13.02.2018.
 */
public class MonumentAdapter extends RecyclerView.Adapter<MonumentAdapter.MonumentHolder>{
    final private List<MonumentEntity> monuments;

    public class MonumentHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.profile_image)
        CircleImageView image;
        @BindView(R.id.name)
        TextView name;
        private View v;
        private Context context;
        public MonumentHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            this.v = v;
            this.context = v.getContext();
        }

        public Context getContext(){
            return context;
        }
    }


    public MonumentAdapter(List<MonumentEntity> monuments) {
        this.monuments = monuments;
    }

    @Override
    public MonumentHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_name, parent, false);
        MonumentHolder vh = new MonumentHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(MonumentHolder holder, int position) {
        holder.name.setMaxLines(1);
        MonumentEntity monument = monuments.get(position);
        holder.name.setText(monument.getType2());
        Picasso.with(holder.getContext()).load(monument.getImgs().get(0).getImg()).error(R.drawable.no_photo).into(holder.image);
        holder.v.setOnClickListener(v -> onMonumentClickListener.onClick(monument));
    }

    @Override
    public int getItemCount() {
        return monuments.size();
    }

    private OnMonumentClickListener onMonumentClickListener;
    public interface OnMonumentClickListener{
        void onClick(MonumentEntity monumentEntity);
    }

    public void setOnPersonClickListener(OnMonumentClickListener onMonumentClickListener) {
        this.onMonumentClickListener = onMonumentClickListener;
    }
}
