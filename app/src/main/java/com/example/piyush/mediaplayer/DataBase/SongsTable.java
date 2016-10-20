package com.example.piyush.mediaplayer.DataBase;

/**
 * Created by Piyush on 09-08-2016.
 */
public class SongsTable extends DbStringHelper {

    public static final String TABLE_NAME = "SongS";

    public static final String TABLE_CREATE_COMMAND =
            CREATE
            + TABLE_NAME
            + BRACKET_OPEN
            + Columns.ID + TYPE_INTEGER_PRIMARY_KEY + AUTOINCREMENT + COMMA
            + Columns.TITLE + TYPE_TEXT + COMMA
            + Columns.PATH + TYPE_TEXT + COMMA
            + Columns.ARTIST + TYPE_TEXT + COMMA
            + Columns.ALBUM + TYPE_TEXT + COMMA
            + Columns.ALBUM_ART_PATH + TYPE_TEXT + COMMA
            + Columns.DURATION + TYPE_TEXT
            + BRACKET_CLOSE
            + SEMI_COLON;

}
