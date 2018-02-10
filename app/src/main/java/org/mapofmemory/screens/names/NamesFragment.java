package org.mapofmemory.screens.names;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import org.mapofmemory.R;
import org.mapofmemory.adapters.PersonAdapter;
import org.mapofmemory.entities.PersonInfo;
import org.mapofmemory.screens.monument.MonumentActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by The Tronuo on 05.02.2018.
 */

public class NamesFragment extends MvpFragment<NamesView, NamesPresenter> implements NamesView{
    @BindView(R.id.recyclerView) FastScrollRecyclerView recyclerView;
    @BindView(R.id.progress) ProgressBar progressBar;

    @Override
    public NamesPresenter createPresenter() {
        return new NamesPresenter(getActivity(), getArguments().getInt("place_id"));
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_names, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getPresenter().loadPersons();
    }

    @Override
    public void onPersonLoad(List<PersonInfo> persons) {
        progressBar.setVisibility(View.GONE);
        PersonAdapter personAdapter = new PersonAdapter(persons);
        personAdapter.setOnPersonClickListener(personInfo -> {
            Intent newInt = new Intent(getActivity(), MonumentActivity.class);
            newInt.putExtra("monument_id", personInfo.getNum() + "");
            newInt.putExtra("image_url", personInfo.getImage());
            startActivity(newInt);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(personAdapter);
    }

    public static NamesFragment newInstance(int placeId) {
        Bundle bundle = new Bundle();
        bundle.putInt("place_id", placeId);
        NamesFragment myFragment = new NamesFragment();
        myFragment.setArguments(bundle);
        return myFragment;
    }
}
