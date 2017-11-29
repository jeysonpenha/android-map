package br.com.exam.androidmap.presenter;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by yande on 28/11/2017.
 */

public interface MapPresenter {

    void onLocationChanged(Location location, GoogleMap googleMap);

}
