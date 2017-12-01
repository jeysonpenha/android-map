package br.com.exam.androidmap.model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import br.com.exam.androidmap.R;
import br.com.exam.androidmap.presenter.MapPresenter;

public class GeocodeTask extends AsyncTask<String, Void, Address> {

    private Context context;
    private MapPresenter presenter;

    public GeocodeTask(Context context, MapPresenter presenter) {
        this.context = context;
        this.presenter = presenter;
    }

    @Override
    protected Address doInBackground(String... locationName) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocationName(locationName[0], 1);

            if(addresses == null || addresses.size() == 0){
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return addresses.get(0);
    }

    @Override
    protected void onPostExecute(Address address) {
        if(address != null) {
            Toast.makeText(context, context.getString(R.string.dialog_search_ok) , Toast.LENGTH_SHORT).show();
            presenter.goToLocation(address.getLatitude(), address.getLongitude(), 15f);

            Marker marker = new Marker();

            marker.name = address.getAddressLine(0);
            marker.latitude = address.getLatitude();
            marker.longitude = address.getLongitude();

            presenter.drawMarker(marker, "", 1);
        } else {
            Toast.makeText(context, context.getString(R.string.dialog_search_error) , Toast.LENGTH_SHORT).show();
        }
    }
}
