package com.example.piyush.mediaplayer.MediaPlayback;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.util.Log;

import com.example.piyush.mediaplayer.DataBase.Columns;
import com.example.piyush.mediaplayer.DataBase.DBOpener;
import com.example.piyush.mediaplayer.DataBase.SongsTable;
import com.example.piyush.mediaplayer.Library.LibraryActivity;
import com.example.piyush.mediaplayer.MediaPlayback.Queue.SongQueue;
import com.example.piyush.mediaplayer.Model.Song;

import java.io.IOException;

/**
 * Created by Piyush on 22-08-2016.
 */
public class Playback extends PlaybackStates{

    private static final String TAG = "Playback";
    private static MediaPlayer mediaPlayer;
    private static Song currSong;
    private static String currSongPath;
    private static SQLiteDatabase songsDb;
    private static String currentState = NOT_STARTED;
    private static OnSongChangedListner mOnSongChangedListner;

    public static void play(String path, Context context) {
        Log.d(TAG, "play: ");
        play(path);
        SongQueue.makeQueue(context);
    }

    private static void play(String path) {
        currSongPath = path;
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        if (currentState == PLAYING || currentState == PAUSED) {
            mediaPlayer.reset();
        }
        try {
            Log.d(TAG, "play: " + path);
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            currentState = PLAYING;
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // play next song
                playNext();
            }
        });
    }

    // returns null if not playing
    public static Song getCurrentSong(Context context) {
        songsDb = DBOpener.openReadableDataBase(context);
        if (currentState == NOT_STARTED || currentState == STOPPED) {
            Log.d(TAG, "getCurrentSong: null");
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

    public static void seekSong(int position) {
        mediaPlayer.seekTo(position);
    }

    public static void playNext() {
        Song next = SongQueue.getNextSong();
        Log.d(TAG, "playNext: " + next.getPath());
        play(next.getPath());
        currSong = next;
        currSongPath = next.getPath();
        currentState = PLAYING;

        if (mOnSongChangedListner != null) {
            mOnSongChangedListner.onSongChanged();
        }
    }

    public static void playPrev() {
        Song prev = SongQueue.getPrevSong();
        if (prev == null) {
            return;
        }
        play(prev.getPath());
        currSong = prev;
        currSongPath = prev.getPath();
        currentState = PLAYING;

        if (mOnSongChangedListner != null) {
            mOnSongChangedListner.onSongChanged();
        }
    }

    public static String getCurrentState() {
        return currentState;
    }

    public static void pause() {
        mediaPlayer.pause();
        currentState = PAUSED;
    }

    public static void resume() {
        mediaPlayer.start();
        currentState = PLAYING;
    }

    public static void setOnSongChangedListner(OnSongChangedListner onSongChangedListner) {
        mOnSongChangedListner = onSongChangedListner;
    }

    public interface OnSongChangedListner {
        void onSongChanged();
    }

    public static int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }
}