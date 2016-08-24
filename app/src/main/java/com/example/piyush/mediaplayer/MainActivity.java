package com.example.piyush.mediaplayer;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
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
import com.example.piyush.mediaplayer.Model.Song;

import java.net.URI;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView songName, artistName, albumName;
    private SeekBar seekBar;
    private ImageView albumArt;
    private ImageView prevBtn, playPauseBtn, nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        songName = (TextView) findViewById(R.id.tvSongName);
        albumName = (TextView) findViewById(R.id.tvAlbumName);
        artistName = (TextView) findViewById(R.id.tvArtistName);
        albumArt = (ImageView) findViewById(R.id.ivAlbumArt);

        Log.d(TAG, "onCreate: called");

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
    }

    public void loadSongs() {
        Log.d(TAG, "loadSongs: ");
        Songs.loadSongsIntoDb(this);
    }

    public void playPrevSong(View view) {

    }

    public void playPause(View view) {

    }

    public void playNextSong(View view) {
    }

    public void openLibraryActivity(View view) {
        Intent intent = new Intent(this, LibraryActivity.class);
        startActivity(intent);
    }

    public void refreshActivity() {
        Log.d(TAG, "refreshActivity: called");
        if (Playback.getCurrentlyPlayingSong(this) == null) {
            Log.d(TAG, "refreshActivity: no song playing");
            return;
        }

        Song currentlyPlaying = Playback.getCurrentlyPlayingSong(this);

        songName.setText(currentlyPlaying.getTitle());
        artistName.setText(currentlyPlaying.getArtist());
        albumName.setText(currentlyPlaying.getAlbum());
        albumArt.setImageURI(Uri.parse(currentlyPlaying.getAlbumArtPath()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshActivity();
    }
}
