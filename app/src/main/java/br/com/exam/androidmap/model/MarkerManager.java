package br.com.exam.androidmap.model;

import java.util.List;

public interface MarkerManager {

    List<Marker> getAllMarkers();

    void deleteMarker(Integer id);

    void addMarker(Marker marker);

}
