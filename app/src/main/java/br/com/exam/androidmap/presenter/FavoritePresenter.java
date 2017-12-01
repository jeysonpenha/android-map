package br.com.exam.androidmap.presenter;

import android.content.SharedPreferences;

import java.util.List;

import br.com.exam.androidmap.model.Marker;

public interface FavoritePresenter {

    void readCloudBookmarkList(SharedPreferences settings, String favListRead);

    List<Marker> getBookmarkList();

    void goToLocationFromFavorite(double lat, double lon, float zoom);

    void deleteBookmark(Marker marker);

}
