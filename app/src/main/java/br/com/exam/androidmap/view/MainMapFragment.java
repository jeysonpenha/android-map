package br.com.exam.androidmap.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import br.com.exam.androidmap.R;
import br.com.exam.androidmap.core.Util;
import br.com.exam.androidmap.model.MapInteractor;
import br.com.exam.androidmap.model.MapInteractorImpl;
import br.com.exam.androidmap.presenter.MapPresenter;
import br.com.exam.androidmap.presenter.MapPresenterImpl;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class MainMapFragment extends Fragment implements MainMapView {

    public static final int LOCATION_PERMISSION_ID = 150;
    public static final String PREFS_MAP = "mapsPref";

    private MapPresenter presenter;

    private GoogleMap googleMap;
    private MapView mapView;
    private Activity activity;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapInteractor interactor = new MapInteractorImpl(this.getContext());
        presenter = new MapPresenterImpl(this, interactor);

        initFavoriteList();
        initLocationManager();

        View view = inflater.inflate(R.layout.fragment_map, null);

        FloatingActionButton saveButton = view.findViewById(R.id.save_button);
        FloatingActionButton actualPosButton = view.findViewById(R.id.actual_pos_button);
        FloatingActionButton searchButton = view.findViewById(R.id.search_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
            }
        });

        actualPosButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    goToActualLocation();
                }
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
            }
        });

        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("StringFormatInvalid")
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                if(checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            LOCATION_PERMISSION_ID
                    );
                } else {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, locationListener);
                }

                recoverLastPosition();
                presenter.initInternalList();
            }
        });

        MapsInitializer.initialize(activity);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            activity =(Activity) context;
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            this.activity = activity;
        }
    }

    @Override
    public void onStop(){
        super.onStop();

        if(googleMap != null) {
            storeLastPosition(
                    googleMap.getCameraPosition().target.latitude,
                    googleMap.getCameraPosition().target.longitude,
                    googleMap.getCameraPosition().zoom);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch ( requestCode ) {
            case LOCATION_PERMISSION_ID: {
                if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    if(checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, locationListener);
                    }
                }
            }
        }
    }

    @Override
    public void createMarker(final double latitude, final double longitude, final String title, final String desc) {
        final LatLng position = new LatLng(latitude, longitude);

        activity.runOnUiThread(new Runnable(){
            public void run(){
                googleMap.addMarker(
                        new MarkerOptions()
                                .position(position)
                                .title(title)
                                .snippet(desc));
            }
        });
    }

    @Override
    public void goToLocation(double latitude, double longitude, float zoom){
        if(googleMap != null) {
            LatLng startPos = new LatLng(latitude, longitude);

            CameraPosition cameraPosition =
                    new CameraPosition
                            .Builder()
                            .target(startPos)
                            .zoom(zoom)
                            .build();

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    public void storeLastPosition(double lat, double lon, float zoom) {
        SharedPreferences settings = activity.getSharedPreferences(PREFS_MAP, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor = Util.putPreferencesDouble(editor,
                activity
                        .getResources()
                        .getString(R.string.last_latitude)
                , lat);

        editor = Util.putPreferencesDouble(editor,
                activity
                        .getResources()
                        .getString(R.string.last_longitude)
                , lon);

        editor.putFloat(activity
                    .getResources()
                    .getString(R.string.last_zoom)
                , zoom);

        editor.apply();
    }

    public void recoverLastPosition() {
        SharedPreferences settings = activity.getSharedPreferences(PREFS_MAP, 0);

        if(settings.contains(activity
                .getResources()
                .getString(R.string.last_latitude))) {

            double lat = Util.getPreferencesDouble(settings, activity
                                                                .getResources()
                                                                .getString(R.string.last_latitude));

            double lon = Util.getPreferencesDouble(settings, activity
                                                                .getResources()
                                                                .getString(R.string.last_longitude));

            float zoom = settings.getFloat(activity
                                                .getResources()
                                                .getString(R.string.last_zoom), 0f);

            goToLocation(lat, lon, zoom);
        }
    }

    public void initFavoriteList(){
        SharedPreferences settings = activity.getSharedPreferences(PREFS_MAP, 0);
        if(!settings.contains(activity
                .getResources()
                .getString(R.string.fav_list_read))) {

            presenter.readFavoriteList();
            SharedPreferences.Editor editor = settings.edit();

            editor.putBoolean(activity
                            .getResources()
                            .getString(R.string.fav_list_read)
                    , true);

            editor.apply();
        }
    }

    public void initStartPosition(Location location){
        createMarker(
                location.getLatitude(),
                location.getLongitude(),
                activity.getResources().getString(R.string.init_mark_title),
                activity.getResources().getString(R.string.init_mark_desc));


        SharedPreferences settings = activity.getSharedPreferences(PREFS_MAP, 0);

        if(!settings.contains(activity
                .getResources()
                .getString(R.string.last_latitude))) {
            goToLocation(location.getLatitude(), location.getLongitude(), 10f);
        }
    }

    public void initLocationManager(){
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                initStartPosition(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}

            @Override
            public void onProviderEnabled(String s) {}

            @Override
            public void onProviderDisabled(String s) {}
        };
    }

    public void goToActualLocation(){
        if(checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            final Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(lastKnownLocation != null) {
                goToLocation(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), 10f);
            } else {
                LocationListener locationActualListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        goToLocation(location.getLatitude(), location.getLongitude(), 10f);
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {}

                    @Override
                    public void onProviderEnabled(String s) {}

                    @Override
                    public void onProviderDisabled(String s) {}
                };


                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, locationActualListener);

            }
        } else {
            Toast.makeText(activity, activity.getString(R.string.no_location_permission) , Toast.LENGTH_SHORT).show();
        }
    }
}
