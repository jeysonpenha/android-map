package br.com.exam.androidmap.presenter;

import br.com.exam.androidmap.model.Marker;

public interface MapPresenter {

    void searchAddress(String name);

    void goToLocation(double lat, double lon, float zoom);

    void drawMarker(Marker marker, String desc, int type);

    void addBookmark(double lat, double lon, String name);

    void drawAllBookmarkList();

}
