package br.com.exam.androidmap.model;

import java.util.List;

import br.com.exam.androidmap.presenter.MapPresenter;

public interface MapInteractor {

    void init(MapPresenter presenter);

    void searchAddress(String name);

    void readCloudBookmarkList();

    List<Marker> getMarkersList();

    void addMarker(Marker marker);

    void removeMarker(int id);
}
