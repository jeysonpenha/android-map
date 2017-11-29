package br.com.exam.androidmap.model;

import java.util.List;

/**
 * Created by yande on 28/11/2017.
 */

public interface MarkerManager {

    Marker getMarker(Integer id);
    List<Marker> getAllMarkers();

}
