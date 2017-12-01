package br.com.exam.androidmap.view;

import java.util.List;

import br.com.exam.androidmap.model.Marker;

public interface MainActivityView {

    void updateBookmarkList(List<Marker> markers);

    void closeDrawer();

    void goToLocationFromFavorite(double lat, double lon, float zoom);

    void updateMarkersFromFavorite(List<Marker> markers);

}
