package br.com.exam.androidmap.presenter;

import android.location.Address;

import br.com.exam.androidmap.model.Marker;

public interface MapPresenter {

    void searchAddress(String name);

    void goToAddress(Address address, float zoom);

    void createMarker(Marker marker, String desc);

    void goToFavorite(Marker marker);

    void deleteFavorite(Marker marker);

    void addFavorite(Marker marker);

    void readFavoriteList();

}
