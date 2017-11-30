package br.com.exam.androidmap.view;

import br.com.exam.androidmap.presenter.MapPresenter;

public interface MainMapView {

    void setPresenter(MapPresenter presenter);

    void drawMarker(double latitude, double longitude, String title, String desc);

    void goToLocation(double latitude, double longitude, float zoom);

}
