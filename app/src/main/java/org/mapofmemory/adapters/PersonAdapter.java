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

import com.afollestad.materialdialogs.MaterialDialog;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;
import com.squareup.picasso.Picasso;

import org.mapofmemory.R;
import org.mapofmemory.entities.PersonInfo;
import org.mapofmemory.entities.RouteInfo;

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
    private List<PersonInfo> personInfos;

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


    public PersonAdapter(List<PersonInfo> personInfos) {
        this.personInfos = personInfos;
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
        return personInfos.get(position).getName().substring(0, 1);
    }

    @Override
    public void onBindViewHolder(PersonHolder holder, int position) {
        holder.name.setMaxLines(1);
        PersonInfo personInfo = personInfos.get(position);
        Picasso.with(holder.getContext()).load(personInfo.getImage()).into(holder.image);
        if (personInfo.getName().contains("\n")){
            holder.name.setMaxLines(2);
        }
        if (personInfo.getInners().size() >= 1){
            holder.v.setOnClickListener(v -> {
                PersonAdapter personAdapter = new PersonAdapter(Observable.fromIterable(personInfo.getInners())
                        .map(person -> {
                            person.setName(person.getType());
                            person.setInners(new ArrayList<>());
                            return person;
                        })
                        .toList()
                        .blockingGet());
                MaterialDialog mDialog = new MaterialDialog.Builder(holder.getContext())
                        .adapter(personAdapter, null)
                        .show();
                personAdapter.setOnPersonClickListener(x ->{
                    //mDialog.dismiss();
                    PersonInfo p = new PersonInfo();
                    p.setName(x.getName());
                    p.setNum(x.getNum());
                    p.setImage(x.getImage());
                    onPersonClickListener.onClick(p);
                });
            });
        }
        else{
            holder.v.setOnClickListener(v -> onPersonClickListener.onClick(personInfo));
        }
        holder.name.setText(personInfo.getName());
    }

    @Override
    public int getItemCount() {
        return personInfos.size();
    }

    private OnPersonClickListener onPersonClickListener;
    public interface OnPersonClickListener{
        void onClick(PersonInfo personInfo);
    }

    public void setOnPersonClickListener(OnPersonClickListener onPersonClickListener) {
        this.onPersonClickListener = onPersonClickListener;
    }
}
