package br.com.exam.androidmap.presenter;

import android.location.Address;

import java.util.List;

import br.com.exam.androidmap.model.MapInteractor;
import br.com.exam.androidmap.model.Marker;
import br.com.exam.androidmap.view.MainMapFragmentView;

public class MapPresenterImpl implements MapPresenter, MapInteractor.OnSearchFinishedListener {

    private MainMapFragmentView mainMapFragmentView;
    private MapInteractor mapInteractor;

    public MapPresenterImpl(MainMapFragmentView mainMapFragmentView, MapInteractor mapInteractor) {
        this.mainMapFragmentView = mainMapFragmentView;
        this.mapInteractor = mapInteractor;
        this.mainMapFragmentView.setPresenter(this);
    }

    @Override
    public void searchAddress(String name){
        mapInteractor.searchAddress(name, this);
    }

    @Override
    public void goToLocation(double lat, double lon, float zoom) {
        mainMapFragmentView.goToLocation(lat, lon, zoom);
    }

    @Override
    public void drawMarker(Marker marker, String desc, int type){
        mainMapFragmentView.drawMarker(marker.latitude, marker.longitude, marker.name, desc, type);
    }

    @Override
    public void addBookmark(double lat, double lon, String name) {
        Marker marker = new Marker();

        marker.name = name;
        marker.latitude = lat;
        marker.longitude = lon;

        mapInteractor.addMarker(marker);

        mainMapFragmentView.updateBookmarkList(mapInteractor.getMarkersList());
    }

    @Override
    public void drawAllBookmarkList() {
        mainMapFragmentView.cleanMarkers();
        List<Marker> markers = mapInteractor.getMarkersList();
        mainMapFragmentView.drawAllMarkers(markers);
    }

    @Override
    public void onSearchFinished(Address address) {
        goToLocation(address.getLatitude(), address.getLongitude(), 15f);

        Marker marker = new Marker();

        marker.name = address.getAddressLine(0);
        marker.latitude = address.getLatitude();
        marker.longitude = address.getLongitude();

        drawMarker(marker, "", 1);
    }
}
