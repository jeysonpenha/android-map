package br.com.exam.androidmap.presenter;

import android.content.SharedPreferences;

import java.util.List;

import br.com.exam.androidmap.model.MapInteractor;
import br.com.exam.androidmap.model.Marker;
import br.com.exam.androidmap.view.MainActivityView;

public class FavoritePresenterImpl implements FavoritePresenter, MapInteractor.OnCloudBookmarkListener {

    private MapInteractor mapInteractor;
    private MainActivityView mainActivityView;

    public FavoritePresenterImpl(MainActivityView mainActivityView, MapInteractor mapInteractor){
        this.mapInteractor = mapInteractor;
        this.mainActivityView = mainActivityView;
    }

    @Override
    public void readCloudBookmarkList(SharedPreferences settings, String favListRead) {
        if(!settings.contains(favListRead)) {
            mapInteractor.readCloudBookmarkList(this);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(favListRead, true);
            editor.apply();
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

    @Override
    public void deleteBookmark(Marker marker) {
        mainActivityView.closeDrawer();

        mapInteractor.removeMarker(marker.id);

        List<Marker> markers = mapInteractor.getMarkersList();

        mainActivityView.updateBookmarkList(markers);
        mainActivityView.updateMarkersFromFavorite(markers);
    }

    @Override
    public void goToLocationFromFavorite(double lat, double lon, float zoom) {
        mainActivityView.closeDrawer();
        mainActivityView.goToLocationFromFavorite(lat, lon, zoom);
    }

    @Override
    public void onCloudBookmarkFinished() {
        List<Marker> markers = mapInteractor.getMarkersList();
        mainActivityView.updateBookmarkList(markers);
        mainActivityView.updateMarkersFromFavorite(markers);
    }
}
