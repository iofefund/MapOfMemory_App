package org.mapofmemory.screens.main;

import android.Manifest;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.hannesdorfmann.mosby3.mvp.MvpActivity;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.mapofmemory.R;
import org.mapofmemory.entities.PlaceEntity;
import org.mapofmemory.screens.about.AboutFragment;
import org.mapofmemory.screens.dom.DOMFragment;
import org.mapofmemory.screens.map.MapFragment;
import org.mapofmemory.screens.names.NamesFragment;
import org.mapofmemory.screens.route.RouteFragment;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;

public class MainActivity extends MvpActivity<MainView, MainPresenter>
        implements NavigationView.OnNavigationItemSelectedListener, MainView {
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.frame)
    FrameLayout frame;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    private TextView placeNameView;
    private MaterialSpinner spinner;
    private TextView placeTitleView;
    private Menu menu;
    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter(getApplicationContext(), getIntent().getIntExtra("place_id", -1));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        spinner = (MaterialSpinner) navigationView.getHeaderView(0).findViewById(R.id.spinner);
        placeTitleView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.place_title);
        placeNameView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.name);
        navigationView.setNavigationItemSelectedListener(this);
        spinner.setOnItemSelectedListener((MaterialSpinner view, int position, long id, Object item) -> {
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().getFragments().get(0)).commit();
            drawer.closeDrawers();
            getPresenter().changePlace(position);
            getPresenter().loadPlaces();
        });
        getPresenter().loadPlaces();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home){
            super.onBackPressed();
        }
        else if (id == R.id.nav_about){
            onAboutFragment(getPresenter().getPlaceId());
        }
        else if (id == R.id.nav_route){
            onRouteFragment(getPresenter().getPlaceId());
        }
        else if (id == R.id.nav_articles){
            onDOMFragment(getPresenter().getPlaceId());
        }
        else if (id == R.id.nav_monuments){
            getPresenter().loadPlaces();
        }
        else if (id == R.id.nav_names){
            onPersonsFragment(getPresenter().getPlaceId());
        }
        else if (id == R.id.nav_lev){
            getPresenter().changePlace(1);
        }
        else if (id == R.id.nav_sand){
            getPresenter().changePlace(0);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPlacesLoad(List<PlaceEntity> places, PlaceEntity currentPlace) {
        String[] titles = new String[places.size()];
        for (int i = 0; i <= places.size() - 1; i++){
            titles[i] = places.get(i).getTitle();
        }
        spinner.setPadding(0, 0, 0, 0);
        spinner.setItems(titles);
        navigationView.getMenu().findItem(R.id.nav_about).setTitle(currentPlace.getAboutTitle());
    }

    @Override
    public void onPlaceSelected(PlaceEntity place, int index) {
        spinner.setSelectedIndex(index);
        placeTitleView.setText(place.getTitle());
        placeNameView.setText(place.getTitle());
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                navigationView.getMenu().findItem(R.id.nav_about).setTitle(place.getAboutTitle());
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        getSupportActionBar().setTitle(place.getTitle());
        onMapFragment(place.getLat(), place.getLng());
    }

    @Override
    public void onPersonsFragment(int placeId) {
        //getSupportActionBar().setTitle("Имена");
        MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.frame, NamesFragment.newInstance(getPresenter().getPlaceId())).commit();

    }

    @Override
    public void onDOMFragment(int placeId) {
        //getSupportActionBar().setTitle("Дни памяти");
        MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.frame, DOMFragment.newInstance(getPresenter().getPlaceId())).commit();
    }

    @Override
    public void onMapFragment(double lat, double lng) {
        //getSupportActionBar().setTitle("Памятники");
        MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.frame, MapFragment.newInstance(getPresenter().getPlaceId(), lat, lng)).commit();
    }

    @Override
    public void onAboutFragment(int placeId) {
        //getSupportActionBar().setTitle(getPresenter().getCurrentPlace().getAboutTitle());
        MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.frame, AboutFragment.newInstance(placeId)).commit();
    }

    @Override
    public void onRouteFragment(int placeId) {
        //getSupportActionBar().setTitle("Как добраться");
        MainActivity.this.getSupportFragmentManager().beginTransaction().replace(R.id.frame, RouteFragment.newInstance(placeId)).commit();
    }

    @Override
    public void onSearchExpand() {

    }
}
