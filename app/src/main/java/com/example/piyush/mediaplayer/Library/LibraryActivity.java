package com.example.piyush.mediaplayer.Library;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.piyush.mediaplayer.DataBase.Songs;
import com.example.piyush.mediaplayer.MainActivity;
import com.example.piyush.mediaplayer.MediaPlayback.Playback;
import com.example.piyush.mediaplayer.MediaPlayback.PlaybackStates;
import com.example.piyush.mediaplayer.Model.Song;
import com.example.piyush.mediaplayer.R;

import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {


    private static final String TAG = "LibAct";
    private Context context = this;
    private RecyclerView songsRecyclerView;

    private static TextView songName, artistName;
    private static ImageView albumArt;
    private static ImageView playPauseBtn;

    private ArrayList<Song> songs;

    private Song currentlyPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        songName = (TextView) findViewById(R.id.tvSongName_Lib);
        artistName = (TextView) findViewById(R.id.tvArtistName_Lib);
        albumArt = (ImageView) findViewById(R.id.ivAlbumArt_Lib);
        playPauseBtn = (ImageView) findViewById(R.id.ivPlayPauseBtn_Lib);

        songsRecyclerView = (RecyclerView) findViewById(R.id.song_list_rv);
        refreshList();

        Playback.setOnSongChangedListener(new Playback.OnSongChangedListener() {
            @Override
            public void onSongChanged() {
                Log.d(TAG, "onSongChanged: called");
                refreshList();
            }
        });
        Playback.setOnPlayPauseListener(new Playback.OnPlayPauseListener() {
            @Override
            public void onPlayPause() {
                refreshList();
            }
        });

    }

    public void refreshList() {
        Log.d(TAG, "refreshList: called");

        //TODO if any problem with list
        if (songs == null) {
            songs = Songs.getSongs(this.getApplicationContext());
        }

            currentlyPlaying = Playback.getCurrentSong(this.getApplicationContext());
        if (currentlyPlaying != null) {
            Log.d(TAG, "refreshListCP: =" + currentlyPlaying.getTitle());
            songName.setText(currentlyPlaying.getTitle());
            artistName.setText(currentlyPlaying.getArtist());
            if (currentlyPlaying.getAlbumArtPath() != null) {
                albumArt.setImageURI(Uri.parse(currentlyPlaying.getAlbumArtPath()));
            }
        }

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

        SongRecyclerViewAdapter songRecyclerViewAdapter = new SongRecyclerViewAdapter(songs);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        songsRecyclerView.setLayoutManager(layoutManager);
        songsRecyclerView.setAdapter(songRecyclerViewAdapter);
        songRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void goToMainActivity(View view) {
        Log.d(TAG, "goToMainActivity: new button");
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void playPause(View view) {
        if (Playback.getCurrentState().equals(PlaybackStates.PLAYING)) {
            Playback.pause();
        }else if (Playback.getCurrentState().equals(PlaybackStates.PAUSED)) {
            Playback.resume();
        }
        refreshList();
    }

    public class SongRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView artist;
        TextView album;

        public SongRecyclerViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.item_title);
            artist = (TextView) itemView.findViewById(R.id.item_Artist);
            album = (TextView) itemView.findViewById(R.id.item_Album);
        }
    }

    public class SongRecyclerViewAdapter extends RecyclerView.Adapter<SongRecyclerViewHolder> {

        private ArrayList<Song> songs;

        public SongRecyclerViewAdapter(ArrayList<Song> songs) {
            this.songs = songs;
        }

        @Override
        public SongRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater li = getLayoutInflater();
            View itemView = li.inflate(R.layout.rv_list_item, parent, false);


            final LibraryActivity.SongRecyclerViewHolder songRecyclerViewHolder = new SongRecyclerViewHolder(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Playback.play(songs.get(songRecyclerViewHolder.getLayoutPosition()).getPath(),context);
                }
            });

            return songRecyclerViewHolder;
        }

        @Override
        public void onBindViewHolder(SongRecyclerViewHolder holder, int position) {
            Song s = songs.get(position);
            if (s.getTitle() != null && s.getTitle() != "") {
                holder.album.setText(s.getAlbum());
                holder.title.setText(s.getTitle());
                holder.artist.setText(s.getArtist());
            } else {
                Log.d(TAG, "onBindViewHolder: "+s.getPath());
                holder.title.setText(s.getPath().substring(s.getPath().lastIndexOf("/")));
//                holder.title.setText(s.getPath());
            }
            if (Playback.getCurrentState() != Playback.NOT_STARTED && Playback.getCurrentState() !=Playback.STOPPED) {
                if (s.getTitle() == Playback.getCurrentSong(context).getTitle() && s.getArtist() == Playback.getCurrentSong(context).getArtist()) {
                    holder.album.setTextColor(getColor(R.color.colorAccent));
                    holder.title.setTextColor(getColor(R.color.colorAccent));
                    holder.artist.setTextColor(getColor(R.color.colorAccent));
                }
            }

        }

        @Override
        public int getItemCount() {
            return songs.size();
        }
    }
}