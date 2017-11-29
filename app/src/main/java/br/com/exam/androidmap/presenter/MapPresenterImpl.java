package br.com.exam.androidmap.presenter;

import android.content.SharedPreferences;
import android.location.Address;
import android.location.Location;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

import br.com.exam.androidmap.R;
import br.com.exam.androidmap.core.App;
import br.com.exam.androidmap.core.Util;
import br.com.exam.androidmap.model.GeocodeTask;
import br.com.exam.androidmap.view.MainMapView;

public class MapPresenterImpl implements MapPresenter {

    private MainMapView mainMapView;
    private GoogleMap googleMap;

    public MapPresenterImpl(MainMapView mainMapView) {
        this.mainMapView = mainMapView;
    }

    @Override
    public void initMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng startPos = new LatLng(location.getLatitude(), location.getLongitude());

        googleMap.addMarker(
                new MarkerOptions()
                        .position(startPos)
                        .title(mainMapView.getViewActivity().getResources().getString(R.string.init_mark_title))
                        .snippet(mainMapView.getViewActivity().getResources().getString(R.string.init_mark_desc)));
    }

    @Override
    public void goToAddress(Address address, Float zoom){
        if(googleMap != null) {
            LatLng startPos = new LatLng(address.getLatitude(), address.getLongitude());

            CameraPosition cameraPosition =
                    new CameraPosition
                            .Builder()
                            .target(startPos)
                            .zoom(zoom)
                            .build();

            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void searchAddress(String name){
        GeocodeTask geocodeTask = new GeocodeTask(mainMapView.getViewActivity(), this);
        geocodeTask.execute(name);
    }

    @Override
    public void storeLastPosition(double lat, double lon, float zoom) {
        SharedPreferences settings = mainMapView.getViewActivity().getSharedPreferences(App.PREFS_MAP, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor = Util.putPreferencesDouble(editor,
                mainMapView
                        .getViewActivity()
                        .getResources()
                        .getString(R.string.last_latitude), lat);

        editor = Util.putPreferencesDouble(editor,
                mainMapView
                        .getViewActivity()
                        .getResources()
                        .getString(R.string.last_longitude), lon);

        editor.putFloat(mainMapView
                .getViewActivity()
                .getResources()
                .getString(R.string.last_zoom), zoom);

        editor.apply();
    }

    @Override
    public void recoverLastPosition() {
        SharedPreferences settings = mainMapView.getViewActivity().getSharedPreferences(App.PREFS_MAP, 0);

        if(settings.contains(mainMapView
                .getViewActivity()
                .getResources()
                .getString(R.string.last_latitude))) {

            double lat = Util.getPreferencesDouble(settings,  mainMapView
                    .getViewActivity()
                    .getResources()
                    .getString(R.string.last_latitude));

            double lon = Util.getPreferencesDouble(settings,  mainMapView
                    .getViewActivity()
                    .getResources()
                    .getString(R.string.last_longitude));

            float zoom = settings.getFloat(mainMapView
                    .getViewActivity()
                    .getResources()
                    .getString(R.string.last_zoom), 0f);

            Address lastAddress = new Address(Locale.getDefault());

            lastAddress.setLatitude(lat);
            lastAddress.setLongitude(lon);

            goToAddress(lastAddress, zoom);
        }

    }
}
