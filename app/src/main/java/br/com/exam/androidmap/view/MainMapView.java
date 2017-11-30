package br.com.exam.androidmap.view;

import android.location.Address;

public interface MainMapView {

    void createMarker(Double latitude, Double longitude, String title, String desc);

    void goToAddress(Address address, Float zoom);

}
