package com.example.piyush.mediaplayer.MediaPlayback.Queue;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.piyush.mediaplayer.DataBase.Songs;
import com.example.piyush.mediaplayer.MediaPlayback.Playback;
import com.example.piyush.mediaplayer.Model.Song;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Piyush on 28-08-2016.
 */
public class SongQueue {

    private static final String TAG = "SongQueue";
    private static ArrayList<Song> songs;
    private static Song currSong;
    private static Stack<Song> nextSongs;
    private static Stack<Song> prevSongs;



    public static void makeQueue(Context context) {
        Log.d(TAG, "makeQueue: ");
        songs = Songs.getSongs(context);
        currSong = Playback.getCurrentlyPlayingSong(context);

        nextSongs = new Stack<>();
        prevSongs = new Stack<>();

        int i = 0;
        for (Song s : songs) {
            if (s.getPath().equals(currSong.getPath())) {
                break;
            } else {
                i++;
            }
        }
        for (int j = songs.size() - 1; j > i; j--) {
                nextSongs.push(songs.get(j));
        }

    }

    public static Song getNextSong() {
        Song next = nextSongs.pop();
        Log.d(TAG, "playNext: " + next.getPath());
        prevSongs.push(currSong);
        currSong = next;
        return next;
    }

    public static Song getPrevSong() {
        if (prevSongs == null || prevSongs.size() < 1) {
            Log.d(TAG, "getPrevSong: empty prev Stack");
            return null;
        }
        Song prev = prevSongs.pop();
        nextSongs.push(currSong);
        currSong = prev;
        return prev;
    }
}
