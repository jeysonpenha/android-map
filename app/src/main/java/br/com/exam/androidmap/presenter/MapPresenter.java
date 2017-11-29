package br.com.exam.androidmap.presenter;

import android.location.Address;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;

public interface MapPresenter {

    void initMap(GoogleMap map);
    void onLocationChanged(Location location);

    void goToAddress(Address address, Float zoom);
    void searchAddress(String name);

    void storeLastPosition(double lat, double lon, float zoom);
    void recoverLastPosition();

}
