package br.com.exam.androidmap.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import br.com.exam.androidmap.R;
import br.com.exam.androidmap.presenter.MapPresenter;
import br.com.exam.androidmap.presenter.MapPresenterImpl;

public class MainMapFragment extends Fragment implements MainMapView {

    MapView mMapView;
    private GoogleMap googleMap;
    public static final int REQ_PERMISSION_LOC = 1;
    private LocationListener locationListener;

    private MapPresenter presenter;
    private LocationManager locationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new MapPresenterImpl();

        View view = inflater.inflate(R.layout.fragment_map, null);

        mMapView = view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        initMap();

        return view;
    }

    @Override
    public void onDetach(){
        super.onDetach();

        Log.i(getResources().getString(R.string.debug_category), "onDetach() - MapFragment");
    }

    @Override
    public void onStart(){
        super.onStart();

        Log.i(getResources().getString(R.string.debug_category), "onCreate() - MapFragment");
    }

    @Override
    public void onResume(){
        super.onResume();
        mMapView.onResume();
        Log.i(getResources().getString(R.string.debug_category), "onResume() - MapFragment");
    }

    @Override
    public void onPause(){
        super.onPause();
        mMapView.onPause();
        Log.i(getResources().getString(R.string.debug_category), "onPause() - MapFragment");
    }

    @Override
    public void onStop(){
        super.onStop();
        mMapView.onStop();
        Log.i(getResources().getString(R.string.debug_category), "onStop() - MapFragment");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mMapView.onDestroy();
        Log.i(getResources().getString(R.string.debug_category), "onDestroy() - MapFragment");
    }

    private void initMap(){
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                initLocation();
            }
        });
    }

    private void initLocation(){
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                presenter.onLocationChanged(location, googleMap);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        ActivityCompat.requestPermissions(
                getActivity(),
                new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                REQ_PERMISSION_LOC
        );

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(getResources().getString(R.string.debug_category), "onRequestPermissionsResult() - MapFragment");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch ( requestCode ) {
            case REQ_PERMISSION_LOC: {
                if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    }
                }
            }
        }
    }
}