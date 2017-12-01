package br.com.exam.androidmap.model;

import android.location.Address;

import java.util.List;

public interface MapInteractor {

    void searchAddress(String name, OnSearchFinishedListener listener);

    void readCloudBookmarkList(OnCloudBookmarkListener listener);

    List<Marker> getMarkersList();

    void addMarker(Marker marker);

    void removeMarker(int id);

    interface OnSearchFinishedListener {
        void onSearchFinished(Address address);
    }

    interface OnCloudBookmarkListener {
        void onCloudBookmarkFinished();
    }

}
