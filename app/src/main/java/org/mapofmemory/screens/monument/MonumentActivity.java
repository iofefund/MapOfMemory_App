package org.mapofmemory.screens.monument;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.squareup.picasso.Picasso;

import org.mapofmemory.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MonumentActivity extends MvpActivity<MonumentView, MonumentPresenter> implements MonumentView {
    @BindView(R.id.image) ImageView image;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.text) TextView text;
    @NonNull
    @Override
    public MonumentPresenter createPresenter() {
        return new MonumentPresenter(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monument);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Picasso.with(this).load(getIntent().getStringExtra("image_url")).into(image);
        text.setText(getIntent().getStringExtra("descr"));
    }

}
