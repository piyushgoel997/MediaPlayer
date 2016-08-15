package com.example.piyush.mediaplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.piyush.mediaplayer.DataBase.Songs;

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

//        // Db testing
//        SQLiteDatabase database = DBOpener.openWritableDataBase(this);
//
//        ContentValues values = new ContentValues();
//
//        values.put(Columns.ID, 1);
//        values.put(Columns.TITLE, "in the end");
//        values.put(Columns.ALBUM, "Hybrid Theory :1");
//        values.put(Columns.ARTIST, "Linkin Park");
//        values.put(Columns.PATH, "abcd");
//
//        database.insert(SongsTable.TABLE_NAME, null, values);
//
////        SQLiteDatabase database = DBOpener.openReadableDataBase(this);
//
//        Cursor c = database.query(SongsTable.TABLE_NAME, new String[]{Columns.ID, Columns.TITLE, Columns.PATH, Columns.ARTIST, Columns.ALBUM}, null, null, null, null, null);
//
//        while (c.moveToNext()) {
//            Log.d(TAG, "onCreate: "
//                    + c.getInt(c.getColumnIndex(Columns.ID)) + ","
//                    + c.getString(c.getColumnIndex(Columns.TITLE)) + ","
//                    + c.getString(c.getColumnIndex(Columns.ARTIST)) + ","
//                    + c.getString(c.getColumnIndex(Columns.ALBUM)) + ","
//                    + c.getString(c.getColumnIndex(Columns.PATH))
//            );
//        }


        Songs.loadSongsIntoDb(this);

        Log.d(TAG, "onCreate: ");
    }

    public void playPrevSong(View view) {
    }

    public void playPause(View view) {
    }

    public void playNextSong(View view) {
    }
}
