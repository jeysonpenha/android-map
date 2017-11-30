package br.com.exam.androidmap.view;

public interface MainMapView {

    void createMarker(double latitude, double longitude, String title, String desc);

    void goToLocation(double latitude, double longitude, float zoom);

}
