package br.com.exam.androidmap.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class MarkerManagerImpl implements MarkerManager {

    private static final String TABLE = "marker";
    private static final String ID = "_id";
    private static final String NAME = "name";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";

    private SQLiteManager sqLiteManager;

    public MarkerManagerImpl(SQLiteManager sqLiteManager){
        this.sqLiteManager = sqLiteManager;
    }

    @Override
    public Marker getMarker(Integer id) {

        Cursor cursor;
        Marker marker = null;

        String[] fields =  {ID,NAME,LONGITUDE,LATITUDE};
        String where = ID + "=" + id;

        SQLiteDatabase db = sqLiteManager.getReadableDatabase();

        cursor = db.query(TABLE, fields ,where, null, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();

            marker = new Marker();
            marker.id = cursor.getInt(0);
            marker.name = cursor.getString(1);
            marker.longitude = cursor.getDouble(2);
            marker.latitude = cursor.getDouble(3);

        }

        db.close();
        return marker;
    }

    @Override
    public List<Marker> getAllMarkers() {
        Cursor cursor;
        List<Marker> markers = null;

        String[] fields =  {ID,NAME,LONGITUDE,LATITUDE};
        SQLiteDatabase db = sqLiteManager.getReadableDatabase();

        cursor = db.query(TABLE, fields ,null, null, null, null, null, null);

        if(cursor != null){
            markers = new ArrayList<>();

            while(cursor.moveToNext()) {
                Marker marker = new Marker();
                marker.id = cursor.getInt(0);
                marker.name = cursor.getString(1);
                marker.longitude = cursor.getDouble(2);
                marker.latitude = cursor.getDouble(3);
                markers.add(marker);
            }
        }

        db.close();
        return markers;

    }

    @Override
    public void deleteMarker(Integer id) {
        String where = ID + "=" + id;
        SQLiteDatabase db = sqLiteManager.getReadableDatabase();
        db.delete(TABLE,where,null);
        db.close();
    }

    @Override
    public void addMarker(Marker marker) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = sqLiteManager.getWritableDatabase();

        values.put(NAME, marker.name);
        values.put(LATITUDE, marker.latitude);
        values.put(LONGITUDE, marker.longitude);

        db.insert(TABLE, null, values);
        db.close();
    }
}
