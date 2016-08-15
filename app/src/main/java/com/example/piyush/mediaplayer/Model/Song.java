package com.example.piyush.mediaplayer.Model;

/**
 * Created by Piyush on 09-08-2016.
 */
public class Song {
    private final String title;
    private final String artist;
    private final String album;
    private final String path;
    private final String duration;
    private final String albumArtPath;

    public Song(String title, String artist, String album, String duration, String path, String albumArtPath) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
        this.path = path;
        this.albumArtPath = albumArtPath;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getDuration() {
        return duration;
    }

    public String getPath() {
        return path;
    }

    public String getAlbumArtPath() {
        return albumArtPath;
    }
}
