package org.mapofmemory.screens.dom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.squareup.picasso.Picasso;

import org.mapofmemory.AppConfig;
import org.mapofmemory.R;
import org.mapofmemory.adapters.DOMAdapter;
import org.mapofmemory.entities.DayOfMemory;
import org.mapofmemory.entities.PlaceEntity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by The Tronuo on 04.02.2018.
 */

public class DOMFragment extends MvpFragment<DOMView, DOMPresenter> implements DOMView {
    @BindView(R.id.shortText)
    TextView shortText;
    @BindView(R.id.detailedText)
    TextView detailedText;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.future_dom_image)
    ImageView futureDomImage;
    @BindView(R.id.future_dom_date)
    TextView futureDomDate;
    @BindView(R.id.future_dom_title)
    TextView futureDomTitle;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.dom_block)
    LinearLayout domBlock;
    @BindView(R.id.btn_write)
    View btnWrite;


    @OnClick(R.id.btn_write)
    void onWrite() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, getPresenter().getPlaceEntity().getTitle().toLowerCase().contains("сандор") ? Uri.parse("https://sand.mapofmemory.org/events/") : Uri.parse(getPresenter().getPlaceEntity().getUrl()));
            startActivity(intent);
        } catch (Exception e) {

        }
    }

    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.nested)
    NestedScrollView nestedScrollView;

    @Override
    public DOMPresenter createPresenter() {
        return new DOMPresenter(getActivity(), getArguments().getInt("place_id", -1));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dom, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getPresenter().loadDayOfMemories();
    }

    @Override
    public void onDOMLoad(List<DayOfMemory> doms) {
        List<DayOfMemory> dayOfMemories = doms;
        int isFuture = -1;
        for (int i = 0; i <= dayOfMemories.size() - 1; i++) {
            if (dayOfMemories.get(i).getType().equals("2")) {
                isFuture = i;
                break;
            }
        }
        if (isFuture != -1) {
            DayOfMemory futureDOM = Observable.fromIterable(dayOfMemories)
                    .filter(dayOfMemory -> dayOfMemory.getType().equals("2"))
                    .blockingFirst();
            Picasso.with(getActivity()).load(futureDOM.getImage()).into(futureDomImage);

            futureDomTitle.setText(futureDOM.getTitle());
            dayOfMemories = Observable.fromIterable(dayOfMemories)
                    .filter(dayOfMemory -> dayOfMemory.getType().equals("1"))
                    .toList()
                    .blockingGet();
        } else {
            domBlock.setVisibility(View.GONE);
        }
        if (dayOfMemories.size() > 0) {
            DOMAdapter domAdapter = new DOMAdapter(getActivity().getSupportFragmentManager(), dayOfMemories);
            viewPager.setAdapter(domAdapter);
            viewPager.setPageMargin(20);
        } else {
            viewPager.setVisibility(View.GONE);
        }
        progressBar.setVisibility(View.GONE);
        nestedScrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPlaceLoad(PlaceEntity place) {
        shortText.setText(place.getDomShort());
        detailedText.setText(place.getDomDetailed());
        Picasso.with(getActivity()).load(place.getDomImage()).into(image);
    }

    @Override
    public void onDOMFailure() {
        domBlock.setVisibility(View.GONE);
    }

    public static DOMFragment newInstance(int placeId) {
        Bundle bundle = new Bundle();
        bundle.putInt("place_id", placeId);
        DOMFragment myFragment = new DOMFragment();
        myFragment.setArguments(bundle);
        return myFragment;
    }
}
