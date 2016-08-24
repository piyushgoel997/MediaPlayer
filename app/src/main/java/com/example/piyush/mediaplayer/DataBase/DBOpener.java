package com.example.piyush.mediaplayer.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Piyush on 09-08-2016.
 */
public class DBOpener extends SQLiteOpenHelper {

    private static final int DB_VER = 1;
    private static final String TAG = "DBOpener";
    private static DBOpener songsDataBase = null;
    public static final String DB_NAME = "Songs Database";

    public DBOpener(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public static SQLiteDatabase openReadableDataBase(Context context) {
        Log.d(TAG, "openReadableDataBase: ");
        if (songsDataBase == null) {
            Log.d(TAG, "openReadableDataBase: create new db");
            songsDataBase = new DBOpener(context);
        }
        return songsDataBase.getReadableDatabase();
    }

    public static SQLiteDatabase openWritableDataBase(Context context) {
        Log.d(TAG, "openWritableDataBase: ");
        if (songsDataBase == null) {
            Log.d(TAG, "openWritableDataBase: create new db");
            songsDataBase = new DBOpener(context);
        }
        return songsDataBase.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: ");
        db.execSQL(SongsTable.TABLE_CREATE_COMMAND);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
