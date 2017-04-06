package com.example.android.movie_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class db_helper extends SQLiteOpenHelper {

    public static final String db_name = "movie.db";
    public static final int db_version = 1;
    public static final String TABLE_NAME = "movie";
    public static final String COLUMN_MOVIE_IMG_URL = "movie_img_url";
    public static final String COLUMN_MOVIE_BACKDROP_PATH = "backdrop_path";
    public static final String COLUMN_MOVIE_ID = "movie_id";
    public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
    public static final String COLUMN_RUNTIME = "runtime";
    public static final String COLUMN_VOTE_AVERAGE = "vote_average";
    public static final String COLUMN_OVERVIEW = "overview";
    public static final String COLUMN_ORIGINAL_TITLE = "original_title";

    public static final String CREATE_MOVIE_DB = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_MOVIE_ID + " TEXT, " +
            COLUMN_MOVIE_IMG_URL + " TEXT, " +
            COLUMN_MOVIE_BACKDROP_PATH + " TEXT, " +
            COLUMN_MOVIE_RELEASE_DATE + " TEXT, " +
            COLUMN_OVERVIEW + " TEXT, " +
            COLUMN_ORIGINAL_TITLE + " TEXT, " +
            COLUMN_RUNTIME + " NUMERIC, " +
            COLUMN_VOTE_AVERAGE + " NUMERIC " + ")";

    public db_helper(Context context) {
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_MOVIE_DB);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_MOVIE_DB);
        onCreate(db);

    }
}
