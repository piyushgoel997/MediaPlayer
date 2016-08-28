package com.example.piyush.mediaplayer.Library;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.piyush.mediaplayer.DataBase.Songs;
import com.example.piyush.mediaplayer.MainActivity;
import com.example.piyush.mediaplayer.MediaPlayback.Playback;
import com.example.piyush.mediaplayer.Model.Song;
import com.example.piyush.mediaplayer.R;

import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {


    private static final String TAG = "LibAct";
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        RecyclerView songsRecyclerView = (RecyclerView) findViewById(R.id.song_list_rv);
        ArrayList<Song> songs = Songs.getSongs(this);
        SongRecyclerViewAdapter songRecyclerViewAdapter = new SongRecyclerViewAdapter(songs);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        songsRecyclerView.setLayoutManager(layoutManager);
        songsRecyclerView.setAdapter(songRecyclerViewAdapter);
        songRecyclerViewAdapter.notifyDataSetChanged();
    }

    public void getCurrSong(View view) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
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
            holder.album.setText(s.getAlbum());
            holder.title.setText(s.getTitle());
            holder.artist.setText(s.getArtist());

        }

        @Override
        public int getItemCount() {
            return songs.size();
        }
    }
}


