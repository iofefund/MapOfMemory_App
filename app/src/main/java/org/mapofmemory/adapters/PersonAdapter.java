package org.mapofmemory.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import com.squareup.picasso.Picasso;

import org.mapofmemory.R;
import org.mapofmemory.entities.MonumentEntity;
import org.mapofmemory.entities.MonumentImgEntity;
import org.mapofmemory.entities.PersonEntity;
import org.mapofmemory.entities.RouteInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;

/**
 * Created by The Tronuo on 21.01.2018.
 */

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.PersonHolder> implements FastScrollRecyclerView.SectionedAdapter{
    final private List<PersonEntity> persons;
    private String imgRoot;
    public class PersonHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.profile_image) CircleImageView image;
        @BindView(R.id.name) TextView name;
        private View v;
        private Context context;
        public PersonHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            this.v = v;
            this.context = v.getContext();
        }

        public Context getContext(){
            return context;
        }
    }

    public void setImgRoot(String imgRoot) {
        this.imgRoot = imgRoot;
    }

    private List<MonumentEntity> monuments;
    public PersonAdapter(List<PersonEntity> persons, List<MonumentEntity> monuments) {
        this.persons = persons;
        this.monuments = monuments;
    }

    @Override
    public PersonHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_name, parent, false);
        PersonHolder vh = new PersonHolder(v);
        return vh;
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return persons.get(position).getName().substring(0, 1);
    }

    @Override
    public void onBindViewHolder(PersonHolder holder, int position) {
        holder.name.setMaxLines(1);
        PersonEntity person = persons.get(position);
        Log.d("IMG", person.getImg());
        Picasso.with(holder.getContext()).load(person.getImg()).error(R.drawable.no_photo).into(holder.image);
        holder.itemView.setOnClickListener(v -> {
            List<MonumentEntity> monumentEntities = Observable.fromIterable(monuments)
                    .filter(monumentEntity -> monumentEntity.getBiographyIdsJson().contains(person.getId()))
                    .toList().blockingGet();
            if (monumentEntities.size() > 1){
                for (MonumentEntity monumentEntity : monumentEntities) {
                    for (MonumentImgEntity img : monumentEntity.getImgs()) {
                        if (!img.getImg().contains("http")) img.setImg(imgRoot + img.getImg());
                    }
                }
                MonumentAdapter adapter = new MonumentAdapter(monumentEntities);
                adapter.setOnPersonClickListener(x -> {
                    onPersonClickListener.onClick(x);
                });
                new MaterialDialog.Builder(holder.getContext())
                        .adapter(adapter, null)
                        .show();
            }
            else if (monumentEntities.size() == 1){
                for (MonumentImgEntity img: monumentEntities.get(0).getImgs()){
                    if (!img.getImg().contains("http")) img.setImg(imgRoot + img.getImg());
                }
                onPersonClickListener.onClick(monumentEntities.get(0));
            }
        });
        holder.name.setText(person.getName());
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    private OnPersonClickListener onPersonClickListener;
    public interface OnPersonClickListener{
        void onClick(MonumentEntity monumentEntity);
    }

    public void setOnPersonClickListener(OnPersonClickListener onPersonClickListener) {
        this.onPersonClickListener = onPersonClickListener;
    }
}
