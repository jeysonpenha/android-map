package br.com.exam.androidmap.view;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import br.com.exam.androidmap.R;
import br.com.exam.androidmap.core.App;
import br.com.exam.androidmap.presenter.MapPresenter;
import br.com.exam.androidmap.presenter.MapPresenterImpl;

import static android.support.v4.content.ContextCompat.checkSelfPermission;

public class MainMapFragment extends Fragment implements MainMapView {

    private MapPresenter presenter;
    private GoogleMap googleMap;
    private MapView vMapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        presenter = new MapPresenterImpl(this);
        View view = inflater.inflate(R.layout.fragment_map, null);

        vMapView = view.findViewById(R.id.map);
        vMapView.onCreate(savedInstanceState);
        vMapView.onResume();

        MapsInitializer.initialize(getActivity().getApplicationContext());



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        vMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                presenter.initMap(googleMap);

                requestPermissions(
                        new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                        App.LOCATION_PERMISSION_ID
                );

                presenter.recoverLastPosition();
            }
        });
    }

    @Override
    public void onStop(){
        super.onStop();

        if(googleMap != null) {
            presenter.storeLastPosition(
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
                presenter.onLocationChanged(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {}

            @Override
            public void onProviderEnabled(String s) {}

            @Override
            public void onProviderDisabled(String s) {}
        };

        switch ( requestCode ) {
            case App.LOCATION_PERMISSION_ID: {
                if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                    if(checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, locationListener);
                    }
                }
            }
        }
    }

    @Override
    public FragmentActivity getViewActivity(){
        return getActivity();
    }

}
