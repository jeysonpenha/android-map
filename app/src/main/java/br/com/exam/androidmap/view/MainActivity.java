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
import br.com.exam.androidmap.presenter.MapPresenter;
import br.com.exam.androidmap.presenter.MapPresenterImpl;

import static br.com.exam.androidmap.view.MainMapFragment.PREFS_MAP;

public class MainActivity extends AppCompatActivity implements MainActivityView {

    DrawerLayout drawer;
    RecyclerView menu;
    Toolbar toolbar;

    ActionBarDrawerToggle toggle;
    MapPresenter presenter;
    MarkerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_main);

        drawer = findViewById(R.id.drawer);
        menu = findViewById(R.id.menu);
        toolbar = findViewById(R.id.toolbar);

        initActionBar();

        MapInteractor interactor = new MapInteractorImpl(this);

        MainMapFragment mapFragment = (MainMapFragment) Fragment.instantiate(this, MainMapFragment.class.getName());

        presenter = new MapPresenterImpl(mapFragment, interactor, this);

        mapFragment.init(presenter);

        adapter = new MarkerAdapter(this, presenter);
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

    public void readCloudBookmarkList(){
        SharedPreferences settings = this.getSharedPreferences(PREFS_MAP, 0);
        if(!settings.contains(this
                .getResources()
                .getString(R.string.fav_list_read))) {

            presenter.readCloudBookmarkList();
            SharedPreferences.Editor editor = settings.edit();

            editor.putBoolean(this
                            .getResources()
                            .getString(R.string.fav_list_read)
                    , true);

            editor.apply();
        }
    }
}
