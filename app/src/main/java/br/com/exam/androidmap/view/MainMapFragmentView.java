package br.com.exam.androidmap.view;

import java.util.List;

import br.com.exam.androidmap.model.Marker;
import br.com.exam.androidmap.presenter.MapPresenter;

public interface MainMapFragmentView {

    void setPresenter(MapPresenter presenter);

    void drawMarker(double latitude, double longitude, String title, String desc, int type);

    void drawAllMarkers(List<Marker> markers);

    void goToLocation(double latitude, double longitude, float zoom);

    void cleanMarkers();

    void updateBookmarkList(List<Marker> markers);

}
