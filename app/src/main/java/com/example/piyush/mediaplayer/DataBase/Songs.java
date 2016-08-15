package com.example.piyush.mediaplayer.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.example.piyush.mediaplayer.DataBase.Columns;
import com.example.piyush.mediaplayer.DataBase.DBOpener;
import com.example.piyush.mediaplayer.DataBase.SongsTable;
import com.example.piyush.mediaplayer.Metadata.Metadata;
import com.example.piyush.mediaplayer.Model.Song;

import java.io.File;

/**
 * Created by Piyush on 08-08-2016.
 */
public class Songs {

    private static final String TAG = "Songs";

    // scan songs from the memory and store their list in a database.

    private static SQLiteDatabase songsDb;

    // scans the storage for mp3 files
    private static void findMP3(File currFile) {
        File[] childFiles = currFile.listFiles();
        if (childFiles == null || childFiles.length == 0) {
            return;
        }
        for (File child : childFiles) {
            if (!child.isFile()) {
                findMP3(child);
            } else if (child.isFile()) {
                // if child has last 4 letters as .mp3 then add that file to database
                String path = child.getPath().toString();
                if (path.endsWith(".mp3") || path.endsWith(".MP4") || path.endsWith(".flac") || path.endsWith(".FLAC")) {
                    putInDb(Metadata.retrieveSongMetadata(path));
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

    // opens db and calls findmp3
    public static void loadSongsIntoDb(Context context) {
        Log.d(TAG, "loadSongsIntoDb: called");
        songsDb = DBOpener.openWritableDataBase(context);
        findMP3(Environment.getExternalStorageDirectory());
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

}
