package br.com.exam.androidmap.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import br.com.exam.androidmap.R;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawer;
    RecyclerView menu;
    Toolbar toolbar;

    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_main);

        drawer = findViewById(R.id.drawer);
        menu = findViewById(R.id.menu);
        toolbar = findViewById(R.id.toolbar);

        initActionBar();

        MainMapFragment mapFragment = (MainMapFragment) Fragment.instantiate(this, MainMapFragment.class.getName());
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, mapFragment).commit();
    }

    protected void initActionBar() {
        toolbar.setTitle("");
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
}
