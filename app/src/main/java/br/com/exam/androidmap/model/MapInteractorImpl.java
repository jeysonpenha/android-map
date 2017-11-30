package br.com.exam.androidmap.model;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.exam.androidmap.R;
import br.com.exam.androidmap.presenter.MapPresenter;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MapInteractorImpl implements MapInteractor {

    private Context context;
    private MapPresenter presenter;
    private SQLiteManager sqliteManager;
    private MarkerManager markerManager;

    public MapInteractorImpl(Context context) {
        this.context = context;
        sqliteManager = new SQLiteManager(context);
        markerManager = new MarkerManagerImpl(sqliteManager);
    }

    @Override
    public void init(MapPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void searchAddress(String name) {
        GeocodeTask geocodeTask = new GeocodeTask(context, presenter);
        geocodeTask.execute(name);
    }

    @Override
    public void initInternalList(){
        List<Marker> markers = markerManager.getAllMarkers();

        if(markers != null) {
            for (int i = 0; i < markers.size(); i++) {
                presenter.createMarker(markers.get(i), "");
            }
        }
    }

    @Override
    public void initFavoriteList(){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                    .url(context.getResources().getString(R.string.favorite_url))
                    .build();

        client.newCall(request).enqueue(
                new Callback() {
                    @Override public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override public void onResponse(Call call, Response response) throws IOException {
                        if (!response.isSuccessful()){
                            throw new IOException("Unexpected code " + response);
                        }

                        String jsonData = response.body().string();

                        try {
                            JSONObject jsonObject = new JSONObject(jsonData);
                            JSONArray jsonArray = jsonObject.getJSONArray(context.getResources().getString(R.string.json_favorites));

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject subJsonObject = jsonArray.getJSONObject(i);

                                Marker marker = new Marker();

                                marker.name = subJsonObject.getString(context.getResources().getString(R.string.json_name));
                                marker.latitude = Double.valueOf(subJsonObject.getString(context.getResources().getString(R.string.json_latitude)));
                                marker.longitude = Double.valueOf(subJsonObject.getString(context.getResources().getString(R.string.json_longitude)));

                                markerManager.addMarker(marker);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
    }
}
