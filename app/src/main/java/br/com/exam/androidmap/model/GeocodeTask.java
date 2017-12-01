package br.com.exam.androidmap.model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import br.com.exam.androidmap.R;

public class GeocodeTask extends AsyncTask<String, Void, Address> {

    private Context context;
    private MapInteractor.OnSearchFinishedListener listener;

    public GeocodeTask(Context context, MapInteractor.OnSearchFinishedListener listener) {
        this.context = context;
        this.listener = listener;
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

            listener.onSearchFinished(address);

        } else {
            Toast.makeText(context, context.getString(R.string.dialog_search_error) , Toast.LENGTH_SHORT).show();
        }
    }
}
