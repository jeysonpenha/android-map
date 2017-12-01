package br.com.exam.androidmap.view;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import br.com.exam.androidmap.R;
import br.com.exam.androidmap.model.MapInteractor;
import br.com.exam.androidmap.model.MapInteractorImpl;
import br.com.exam.androidmap.model.Marker;
import br.com.exam.androidmap.presenter.FavoritePresenterImpl;

import static br.com.exam.androidmap.view.MainMapFragment.PREFS_MAP;

public class MainActivity extends AppCompatActivity implements MainActivityView {

    private DrawerLayout drawer;
    private RecyclerView menu;
    private Toolbar toolbar;

    private ActionBarDrawerToggle toggle;
    private FavoritePresenterImpl favoritePresenter;
    private MarkerAdapter adapter;

    private MainMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_main);

        drawer = findViewById(R.id.drawer);
        menu = findViewById(R.id.menu);
        toolbar = findViewById(R.id.toolbar);

        initActionBar();

        MapInteractor interactor = new MapInteractorImpl(this);
        favoritePresenter = new FavoritePresenterImpl(this, interactor);

        mapFragment = (MainMapFragment) Fragment.instantiate(this, MainMapFragment.class.getName());
        adapter = new MarkerAdapter(this, favoritePresenter);
        menu.setAdapter(adapter);

        readCloudBookmarkList();

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, mapFragment).commit();
    }

    protected void initActionBar() {
        toolbar.setTitle("Easy Android Test");
        this.setSupportActionBar(toolbar);
        this.toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);

        drawer.addDrawerListener(this.toggle);
        drawer.post(new Runnable() {
            public void run() {
                toggle.syncState();
            }
        });

        LinearLayoutManager mLayoutManagerI = new LinearLayoutManager(this);
        menu.setLayoutManager(mLayoutManagerI);
    }

    @Override
    public void updateBookmarkList(final List<Marker> markers) {
        this.runOnUiThread(new Runnable() {
            public void run(){
                adapter.updateList(markers);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void closeDrawer(){
        drawer.closeDrawers();
    }

    @Override
    public void goToLocationFromFavorite(double lat, double lon, float zoom) {
        mapFragment.goToLocation(lat, lon, zoom);
    }

    public void readCloudBookmarkList(){
        SharedPreferences settings = this.getSharedPreferences(PREFS_MAP, 0);
        favoritePresenter.readCloudBookmarkList(settings, this
                .getResources()
                .getString(R.string.fav_list_read));
    }

    @Override
    public void updateMarkersFromFavorite(List<Marker> markers){
        mapFragment.cleanMarkers();
        mapFragment.drawAllMarkers(markers);
    }
}
