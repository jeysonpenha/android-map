package br.com.exam.androidmap.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapInteractor interactor = new MapInteractorImpl(this.getContext());
        presenter = new MapPresenterImpl(this, interactor);

        View view = inflater.inflate(R.layout.fragment_map, null);

        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        mapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("StringFormatInvalid")
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                requestPermissions(
                        new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                        LOCATION_PERMISSION_ID
                );

                recoverLastPosition();
            }
        });

        initFavoriteList();

        MapsInitializer.initialize(getActivity().getApplicationContext());

        return view;
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

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                createMarker(
                        location.getLatitude(),
                        location.getLongitude(),
                        getActivity().getResources().getString(R.string.init_mark_title),
                        getActivity().getResources().getString(R.string.init_mark_desc));
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}

            @Override
            public void onProviderEnabled(String s) {}

            @Override
            public void onProviderDisabled(String s) {}
        };

        switch ( requestCode ) {
            case LOCATION_PERMISSION_ID: {
                if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    if(checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, locationListener);
                    }
                }
            }
        }
    }

    @Override
    public void createMarker(Double latitude, Double longitude, String title, String desc) {
        LatLng position = new LatLng(latitude, longitude);

        googleMap.addMarker(
                new MarkerOptions()
                        .position(position)
                        .title(title)
                        .snippet(desc));

    }

    @Override
    public void goToAddress(Address address, Float zoom){
        if(googleMap != null) {
            LatLng startPos = new LatLng(address.getLatitude(), address.getLongitude());

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
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_MAP, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor = Util.putPreferencesDouble(editor,
                getActivity()
                        .getResources()
                        .getString(R.string.last_latitude)
                , lat);

        editor = Util.putPreferencesDouble(editor,
                getActivity()
                        .getResources()
                        .getString(R.string.last_longitude)
                , lon);

        editor.putFloat(getActivity()
                    .getResources()
                    .getString(R.string.last_zoom)
                , zoom);

        editor.apply();
    }

    public void recoverLastPosition() {
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_MAP, 0);

        if(settings.contains(getActivity()
                .getResources()
                .getString(R.string.last_latitude))) {

            double lat = Util.getPreferencesDouble(settings, getActivity()
                                                                .getResources()
                                                                .getString(R.string.last_latitude));

            double lon = Util.getPreferencesDouble(settings, getActivity()
                                                                .getResources()
                                                                .getString(R.string.last_longitude));

            float zoom = settings.getFloat(getActivity()
                                                .getResources()
                                                .getString(R.string.last_zoom), 0f);

            Address lastAddress = new Address(Locale.getDefault());

            lastAddress.setLatitude(lat);
            lastAddress.setLongitude(lon);

            goToAddress(lastAddress, zoom);
        }
    }

    public void initFavoriteList(){
        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_MAP, 0);
        if(!settings.contains(getActivity()
                .getResources()
                .getString(R.string.fav_list_read))) {

            presenter.readFavoriteList();
            SharedPreferences.Editor editor = settings.edit();

            editor.putBoolean(getActivity()
                            .getResources()
                            .getString(R.string.fav_list_read)
                    , true);

            editor.apply();
        }
    }
}
