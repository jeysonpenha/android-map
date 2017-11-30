package br.com.exam.androidmap.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteManager extends SQLiteOpenHelper {

    private static final String BD_NAME = "maps.db";

    private static final String TABLE = "marker";
    private static final String ID = "_id";
    private static final String NAME = "name";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";

    private static final int VERSION = 1;

    public SQLiteManager(Context context){
        super(context, BD_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+ TABLE +" ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME + " TEXT, "
                + LONGITUDE + " DOUBLE, "
                + LATITUDE + " DOUBLE "
                +")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            String sql = "DROP TABLE IF EXISTS " + TABLE;

            db.execSQL(sql);
            onCreate(db);
        }
    }
}
