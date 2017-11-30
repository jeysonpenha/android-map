package br.com.exam.androidmap.model;

import java.util.List;

public interface MarkerManager {

    Marker getMarker(Integer id);

    List<Marker> getAllMarkers();

    void deleteMarker(Integer id);

    void addMarker(Marker marker);

}
