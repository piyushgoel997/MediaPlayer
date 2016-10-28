package com.example.piyush.mediaplayer.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.Log;

import com.example.piyush.mediaplayer.Metadata.Metadata;
import com.example.piyush.mediaplayer.Model.Song;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Piyush on 08-08-2016.
 */
public class Songs {

    private static final String TAG = "Songs";
    public static ArrayList<Song> songs;
    private static SQLiteDatabase songsDb;

    // scan songs from the memory and store their list in a database.


    // scans the storage for mp3 files
    private static void findMP3(File currFile, Context context) {
        File[] childFiles = currFile.listFiles();
        if (childFiles == null || childFiles.length == 0) {
            return;
        }
        for (File child : childFiles) {
            if (!child.isFile()) {
                findMP3(child, context);
            } else if (child.isFile()) {
                // if child has last 4 letters as .mp3 then add that file to database
                String path = child.getPath().toString();
                if (path.endsWith(".mp3") || path.endsWith(".MP4") || path.endsWith(".flac") || path.endsWith(".FLAC")) {
                    putInDb(Metadata.retrieveSongMetadata(path, context));
                }
            }
        }
    }

    // gets info from a song and saves it into a db
    private static void putInDb(Song song) {
        ContentValues values = new ContentValues();
        values.put(Columns.PATH, song.getPath());
        values.put(Columns.ARTIST, song.getArtist());
        values.put(Columns.ALBUM, song.getAlbum());
        values.put(Columns.TITLE, song.getTitle());
        values.put(Columns.ALBUM_ART_PATH, song.getAlbumArtPath());
        values.put(Columns.DURATION, song.getDuration());
        songsDb.insert(SongsTable.TABLE_NAME, null, values);
    }

    // opens db and calls findMP3
    public static void loadSongsIntoDb(Context context) {
        songsDb = DBOpener.openWritableDataBase(context);
        if (isDbEmpty(songsDb)) {
            Log.d(TAG, "loadSongsIntoDb: creating new db");
            findMP3(Environment.getExternalStorageDirectory(),context);
            songsDb.close();
            return;
        } else {
            Log.d(TAG, "loadSongsIntoDb: db already exists");
            songsDb.close();
            return;
        }
    }


    private static boolean isDbEmpty(SQLiteDatabase songsDb) {
        try {
            Cursor c = songsDb.rawQuery("SELECT * FROM " + SongsTable.TABLE_NAME, null);
            if (c.moveToFirst()) {
                Log.d(TAG, "isDbEmpty: not empty");
                return false;
            }
        } catch (SQLiteException e) {
            Log.d(TAG, "isDbEmpty: doesn't exist");
            return true;
        }
        return true;
    }

    //    public static void logAll(Context context) {
//        Log.d(TAG, "logAll: called");
//        songsDb = DBOpener.openReadableDataBase(context);
//        Cursor c = songsDb.query(SongsTable.TABLE_NAME,
//                new String[]{
//                        Columns.PATH
//                },
//                null,
//                null,
//                null,
//                null,
//                null
//        );
//        while (c.moveToNext()) {
//            Log.d(TAG, "logAll: " + c.getString(c.getColumnIndex(Columns.PATH)));
//        }
//    }

    private static void putSongsInArrayList(Context context) {
        Log.d(TAG, "putSongsInArrayList: ");
        songs = new ArrayList<>();
        songsDb = DBOpener.openReadableDataBase(context);
        Cursor c = songsDb.query(SongsTable.TABLE_NAME, new String[]{Columns.ID, Columns.TITLE, Columns.PATH, Columns.ARTIST, Columns.ALBUM, Columns.ALBUM_ART_PATH, Columns.DURATION}, null, null, null, null, Columns.TITLE);
        while (c.moveToNext()) {
            Song s = new Song(
                    c.getString(c.getColumnIndex(Columns.TITLE)),
                    c.getString(c.getColumnIndex(Columns.ARTIST)),
                    c.getString(c.getColumnIndex(Columns.ALBUM)),
                    c.getString(c.getColumnIndex(Columns.DURATION)),
                    c.getString(c.getColumnIndex(Columns.PATH)),
                    c.getString(c.getColumnIndex(Columns.ALBUM_ART_PATH))
            );
            songs.add(s);
        }
        c.close();
    }

    public static ArrayList<Song> getSongs(Context context) {
        Log.d(TAG, "getSongs: ");
        putSongsInArrayList(context);
        return songs;
    }
}
