package br.com.exam.androidmap.presenter;

import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by yande on 28/11/2017.
 */

public class MapPresenterImpl implements MapPresenter {

    @Override
    public void onLocationChanged(Location location, GoogleMap googleMap) {
        LatLng startPos = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(startPos).title("Sua Posição!").snippet("Sua atual posição quando iniciou o aplicativo."));

        CameraPosition cameraPosition = new CameraPosition.Builder().target(startPos).zoom(10).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }



}
