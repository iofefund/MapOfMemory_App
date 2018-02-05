package org.mapofmemory.screens.about;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.mapofmemory.R;
import org.mapofmemory.entities.AboutEntity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by The Tronuo on 04.02.2018.
 */

public class AboutFragment extends MvpFragment<AboutView, AboutPresenter> implements AboutView {
    @BindView(R.id.shortText) TextView shortText;
    @BindView(R.id.image) ImageView imageView;
    @BindView(R.id.detailedText) TextView detailedText;
    @BindView(R.id.progress) ProgressBar progressBar;
    @BindView(R.id.nested) NestedScrollView nestedScrollView;

    @Override
    public AboutPresenter createPresenter() {
        return new AboutPresenter(getActivity(), getArguments().getInt("place_id", -1));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getPresenter().loadAboutInfo();
    }

    @Override
    public void onAboutLoaded(AboutEntity aboutEntity) {
        Log.d("IMAGE", aboutEntity.getImage());
        Picasso.with(getActivity()).load(aboutEntity.getImage()).into(imageView);
        shortText.setText(aboutEntity.getDescrShort());
        detailedText.setText(aboutEntity.getDescrDetailed());
        nestedScrollView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onAboutFailure() {

    }

    public static AboutFragment newInstance(int placeId) {
        Bundle bundle = new Bundle();
        bundle.putInt("place_id", placeId);
        AboutFragment myFragment = new AboutFragment();
        myFragment.setArguments(bundle);
        return myFragment;
    }
}
