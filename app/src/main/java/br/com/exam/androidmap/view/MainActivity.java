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
import android.util.Log;

import br.com.exam.androidmap.R;

/**
 * Created by yande on 28/11/2017.
 */

public class MainActivity extends AppCompatActivity {

    //Components
    DrawerLayout drawerPrincipal;
    RecyclerView menuLateral;
    Toolbar toolbar;

    ActionBarDrawerToggle togglePrincipal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(getResources().getString(R.string.debug_category), "onCreate() - MainActivity");

        //Seta Interface Principal.
        setContentView(R.layout.layout_main);

        //Inicia o menu.
        drawerPrincipal = findViewById(R.id.drawer);
        menuLateral = findViewById(R.id.menu);
        toolbar = findViewById(R.id.toolbar);

        iniciarActionBar(drawerPrincipal, menuLateral, toolbar);

        //Inicia o Fragmento inicial.
        MainMapFragment mapFragment = (MainMapFragment) Fragment.instantiate(this, MainMapFragment.class.getName());
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, mapFragment).commit();
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.i(getResources().getString(R.string.debug_category), "onStop() - MainActivity");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i(getResources().getString(R.string.debug_category), "onDestroy() - MainActivity");
    }

    @Override
    public void onResume(){
        super.onResume();

        Log.i(getResources().getString(R.string.debug_category), "onResume() - MainActivity");
    }

    @Override
    public void onPause(){
        super.onPause();

        Log.i(getResources().getString(R.string.debug_category), "onPause() - MainActivity");
    }

    protected void iniciarActionBar(DrawerLayout drawerPrincipal, RecyclerView menuLateral, Toolbar toolbar) {
        toolbar.setTitle("");
        this.setSupportActionBar(toolbar);
        this.togglePrincipal = new ActionBarDrawerToggle(this, drawerPrincipal, toolbar, R.string.drawer_open, R.string.drawer_close);

        drawerPrincipal.addDrawerListener(this.togglePrincipal);
        drawerPrincipal.post(new Runnable() {
            public void run() {
                togglePrincipal.syncState();
            }
        });

        LinearLayoutManager mLayoutManagerI = new LinearLayoutManager(this);
        menuLateral.setLayoutManager(mLayoutManagerI);
    }

}


