package br.com.exam.androidmap.presenter;

import java.util.List;

import br.com.exam.androidmap.model.MapInteractor;
import br.com.exam.androidmap.model.Marker;
import br.com.exam.androidmap.view.MainActivityView;
import br.com.exam.androidmap.view.MainMapView;

public class MapPresenterImpl implements MapPresenter {

    private MainMapView mainMapView;
    private MainActivityView mainActivityView;

    private MapInteractor mapInteractor;

    public MapPresenterImpl(MainMapView mainMapView, MapInteractor mapInteractor, MainActivityView mainActivityView) {
        this.mainMapView = mainMapView;
        this.mapInteractor = mapInteractor;
        this.mainActivityView = mainActivityView;

        this.mapInteractor.init(this);
        this.mainMapView.setPresenter(this);
    }

    @Override
    public void searchAddress(String name){
        mapInteractor.searchAddress(name);
    }

    @Override
    public void goToLocation(double lat, double lon, float zoom) {
        mainActivityView.closeDrawer();
        mainMapView.goToLocation(lat, lon, zoom);
    }

    @Override
    public void drawMarker(Marker marker, String desc, int type){
        mainMapView.drawMarker(marker.latitude, marker.longitude, marker.name, desc, type);
    }

    @Override
    public void deleteBookmark(Marker marker) {
        mainActivityView.closeDrawer();
        mapInteractor.removeMarker(marker.id);
        mapInteractor.getMarkersList();
        mainActivityView.updateBookmarkList(mapInteractor.getMarkersList());
        drawAllBookmarkList();
    }

    @Override
    public void addBookmark(double lat, double lon, String name) {
        Marker marker = new Marker();

        marker.name = name;
        marker.latitude = lat;
        marker.longitude = lon;

        mapInteractor.addMarker(marker);

        mainActivityView.updateBookmarkList(mapInteractor.getMarkersList());
    }

    @Override
    public void readCloudBookmarkList() {
        mapInteractor.readCloudBookmarkList();
    }

    @Override
    public void updateBookmarkList(){
        mainActivityView.updateBookmarkList(mapInteractor.getMarkersList());
        drawAllBookmarkList();
    }

    @Override
    public void drawAllBookmarkList() {
        mainMapView.cleanMarkers();

        List<Marker> markers = mapInteractor.getMarkersList();

        if(markers != null) {
            for (int i = 0; i < markers.size(); i++) {
                drawMarker(markers.get(i), "", 0);
            }
        }
    }

    @Override
    public List<Marker> getBookmarkList() {
        List<Marker> markers = mapInteractor.getMarkersList();

        if(markers != null) {
            return markers;
        } else {
            return null;
        }
    }
}
