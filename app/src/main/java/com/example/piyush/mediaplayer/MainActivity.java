package com.example.piyush.mediaplayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.piyush.mediaplayer.DataBase.Songs;
import com.example.piyush.mediaplayer.Library.LibraryActivity;
import com.example.piyush.mediaplayer.MediaPlayback.Playback;
import com.example.piyush.mediaplayer.MediaPlayback.PlaybackStates;
import com.example.piyush.mediaplayer.Model.Song;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static TextView songName, artistName, albumName;
    private static SeekBar seekBar;
    private static ImageView albumArt;
    private static ImageView prevBtn, playPauseBtn, nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate: ");

        setContentView(R.layout.activity_main);

        songName = (TextView) findViewById(R.id.tvSongName);
        albumName = (TextView) findViewById(R.id.tvAlbumName);
        artistName = (TextView) findViewById(R.id.tvArtistName);
        albumArt = (ImageView) findViewById(R.id.ivAlbumArt);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        prevBtn = (ImageView) findViewById(R.id.ivPrevBtn);
        playPauseBtn = (ImageView) findViewById(R.id.ivPlayPauseBtn);
        nextBtn = (ImageView) findViewById(R.id.ivNextBtn);

//        loadSongs();

        PermissionCallback permissionCallback = new PermissionCallback() {
            @Override
            public void permissionGranted() {
                Log.d(TAG, "permissionGranted: ");
                loadSongs();
            }

            @Override
            public void permissionRefused() {
                Toast.makeText(MainActivity.this, "Can't play music unless you let me scan your mobile's storage", Toast.LENGTH_SHORT).show();
            }
        };

        Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE,permissionCallback);

        refreshActivity();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    Playback.seekSong(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        seekBar.postDelayed(progressRunnable,10);
    }

    public Runnable progressRunnable = new Runnable() {
        @Override
        public void run() {
            if (seekBar != null) {

                if (Playback.getCurrentState() == PlaybackStates.PLAYING) {
                    int currPos = Playback.getCurrentPosition();
//                        Log.d(TAG, "updateSeekBar: " + currPos);
                    seekBar.setProgress(currPos);
                    seekBar.postDelayed(progressRunnable, 50);
                }
            }
        }
    };

    public void loadSongs() {
        Log.d(TAG, "loadSongs: ");
        Songs.loadSongsIntoDb(this.getApplicationContext());
    }

    public void playPrevSong(View view) {
        Playback.playPrev();
        refreshActivity();
    }

    public void playPause(View view) {
        Log.d(TAG, "playPause: "+Playback.getCurrentState());
        if (Playback.getCurrentState().equals(PlaybackStates.PLAYING)) {
            Playback.pause();
        }else if (Playback.getCurrentState().equals(PlaybackStates.PAUSED)) {
            Playback.resume();
        }
        refreshActivity();
    }

    public void playNextSong(View view) {
        Playback.playNext();
        refreshActivity();
    }

    public void openLibraryActivity(View view) {
        Intent intent = new Intent(this, LibraryActivity.class);
        startActivity(intent);
    }

    public void refreshActivity() {
        Log.d(TAG, "refreshActivity: called");
        if (Playback.getCurrentSong(this.getApplicationContext()) == null) {
            Log.d(TAG, "refreshActivity: no song playing");
            return;
        }

        Song currentlyPlaying = Playback.getCurrentSong(this.getApplicationContext());

        songName.setText(currentlyPlaying.getTitle());
        artistName.setText(currentlyPlaying.getArtist());
        albumName.setText(currentlyPlaying.getAlbum());
        Log.d(TAG, "refreshActivity: " + currentlyPlaying.getAlbumArtPath());
        if (currentlyPlaying.getAlbumArtPath() != null) {
            albumArt.setImageURI(Uri.parse(currentlyPlaying.getAlbumArtPath()));
        }
        if (seekBar.getMax() != Integer.parseInt(currentlyPlaying.getDuration())) {
            seekBar.setMax(Integer.parseInt(currentlyPlaying.getDuration()));

        }

        Log.d(TAG, "refreshActivity: " + currentlyPlaying.getDuration());

        switch (Playback.getCurrentState()) {
            case PlaybackStates.PLAYING:
                playPauseBtn.setImageResource(R.drawable.pause_button);
                break;
            case PlaybackStates.PAUSED:
                playPauseBtn.setImageResource(R.drawable.play_button);
                break;
            case PlaybackStates.STOPPED:
                playPauseBtn.setImageResource(R.drawable.play_button);
                break;
            case PlaybackStates.NOT_STARTED:
                playPauseBtn.setImageResource(R.drawable.play_button);
                break;
        }
    }


    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: called");
        super.onResume();
        refreshActivity();
    }


}