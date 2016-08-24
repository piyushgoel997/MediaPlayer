package com.example.piyush.mediaplayer.MediaPlayback;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.util.Log;

import com.example.piyush.mediaplayer.DataBase.Columns;
import com.example.piyush.mediaplayer.DataBase.DBOpener;
import com.example.piyush.mediaplayer.DataBase.SongsTable;
import com.example.piyush.mediaplayer.Model.Song;

import java.io.IOException;

/**
 * Created by Piyush on 22-08-2016.
 */
public class Playback {

    private static final String TAG = "Playback";
    private static MediaPlayer mediaPlayer;
    private static Song currSong;
    private static String currSongPath;
    private static SQLiteDatabase songsDb;

    public static void play(String path) {
        currSongPath = path;
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
        }
        try {
            Log.d(TAG, "play: " + path);
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // returns null if not playing
    public static Song getCurrentlyPlayingSong(Context context) {
        songsDb = DBOpener.openReadableDataBase(context);
        if (currSongPath == null) {
            return null;
        }
        Cursor c = songsDb.query(SongsTable.TABLE_NAME,
                new String[]{Columns.ID, Columns.TITLE, Columns.PATH, Columns.ALBUM, Columns.ARTIST, Columns.DURATION, Columns.ALBUM_ART_PATH},
                Columns.PATH + " = ?",
                new String[] {currSongPath},
                null,
                null,
                null);
        c.moveToFirst();
        currSong = new Song(c.getString(c.getColumnIndex(Columns.TITLE)),
                c.getString(c.getColumnIndex(Columns.ARTIST)),
                c.getString(c.getColumnIndex(Columns.ALBUM)),
                c.getString(c.getColumnIndex(Columns.DURATION)),
                c.getString(c.getColumnIndex(Columns.PATH)),
                c.getString(c.getColumnIndex(Columns.ALBUM_ART_PATH))
        );
        return currSong;
    }
    
}