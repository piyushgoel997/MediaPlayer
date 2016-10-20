package com.example.piyush.mediaplayer.Metadata;

import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.piyush.mediaplayer.Model.Song;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Piyush on 15-08-2016.
 */
public class Metadata {

    private static final String TAG = "Metadata";

    // retrieves metadata from a file and makes a new song using that metadata and returns it
    public static Song retrieveSongMetadata(String songFilePath) {
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        try {
            metadataRetriever.setDataSource(songFilePath);
        } catch (RuntimeException re) {
            Log.e(TAG, "retrieveSongMetadata: " + songFilePath, re);
        }
        String albumArtPath = null;
        if (metadataRetriever.getEmbeddedPicture() != null) {
            albumArtPath = saveAlbumArt(metadataRetriever.getEmbeddedPicture(), metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE) + "-" + metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        }
        return new Song(
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE),
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST),
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM),
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION),
                songFilePath,
                albumArtPath
        );
    }


    // saves album art to AlbumArt folder and returns its path
    @NonNull
    public static String saveAlbumArt(byte[] embeddedPicture, String fileName) {
        File aaFolder = new File(Environment.getExternalStorageDirectory().getPath().toString() + "/albumart/");
        if (!aaFolder.exists()) {
//            Log.d(TAG, "saveAlbumArt: folder doesn't exist");
            boolean x= aaFolder.mkdirs();
            Log.d(TAG, "saveAlbumArt: "+x);
        }
        File file = new File(aaFolder, fileName);
        if (file.exists()) {
            Log.d(TAG, "saveAlbumArt: file exists");
            return file.getPath();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file.getPath());
            fos.write(embeddedPicture,0,embeddedPicture.length);
            fos.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return file.getPath();
    }

}
