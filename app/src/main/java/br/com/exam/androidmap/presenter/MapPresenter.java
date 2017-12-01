package br.com.exam.androidmap.presenter;

import java.util.List;

import br.com.exam.androidmap.model.Marker;

public interface MapPresenter {

    void searchAddress(String name);

    void goToLocation(double lat, double lon, float zoom);

    void drawMarker(Marker marker, String desc, int type);

    void deleteBookmark(Marker marker);

    void addBookmark(double lat, double lon, String name);

    void readCloudBookmarkList();

    void updateBookmarkList();

    void drawAllBookmarkList();

    List<Marker> getBookmarkList();

}
