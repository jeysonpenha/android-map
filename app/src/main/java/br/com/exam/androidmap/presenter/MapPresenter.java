package br.com.exam.androidmap.presenter;

import android.location.Address;

import java.util.List;

import br.com.exam.androidmap.model.Marker;

public interface MapPresenter {

    void searchAddress(String name);

    void goToLocation(double lat, double lon, float zoom);

    void drawMarker(Marker marker, String desc);

    void goToBookmark(Marker marker);

    void deleteBookmark(Marker marker);

    void addBookmark(double lat, double lon, String name);

    void readCloudBookmarkList();

    void initBookmarkList();

    List<Marker> getBookmarkList();

}
