package br.com.exam.androidmap.presenter;

import android.location.Address;

import br.com.exam.androidmap.model.MapInteractor;
import br.com.exam.androidmap.model.Marker;
import br.com.exam.androidmap.view.MainMapView;

public class MapPresenterImpl implements MapPresenter {

    private MainMapView mainMapView;
    private MapInteractor mapInteractor;

    public MapPresenterImpl(MainMapView mainMapView, MapInteractor mapInteractor) {
        this.mainMapView = mainMapView;
        this.mapInteractor = mapInteractor;

        this.mapInteractor.init(this);
    }

    @Override
    public void searchAddress(String name){
        mapInteractor.searchAddress(name);
    }

    @Override
    public void goToAddress(Address adress, float zoom) {
        mainMapView.goToAddress(adress, zoom);
    }

    @Override
    public void createMarker(Marker marker, String desc){
        mainMapView.createMarker(marker.latitude, marker.longitude, marker.name, desc);
    }

    @Override
    public void goToFavorite(Marker marker) {

    }

    @Override
    public void deleteFavorite(Marker marker) {

    }

    @Override
    public void addFavorite(Marker marker) {

    }

    @Override
    public void readFavoriteList() {
        mapInteractor.initFavoriteList();
    }
}
