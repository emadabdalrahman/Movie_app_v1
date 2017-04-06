package com.example.android.movie_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class db_control {

    SQLiteOpenHelper openHelper;
    SQLiteDatabase database;
    public String[] allcolumns = {
            db_helper.COLUMN_MOVIE_ID,
            db_helper.COLUMN_MOVIE_IMG_URL,
            db_helper.COLUMN_MOVIE_BACKDROP_PATH,
            db_helper.COLUMN_MOVIE_RELEASE_DATE,
            db_helper.COLUMN_OVERVIEW,
            db_helper.COLUMN_ORIGINAL_TITLE,
            db_helper.COLUMN_RUNTIME,
            db_helper.COLUMN_VOTE_AVERAGE};

    public db_control(Context context) {
        openHelper = new db_helper(context);
    }

    public void open_db() {
        database = openHelper.getWritableDatabase();
    }

    public void close_db() {
        openHelper.close();
    }

    public void insert_movie(String id, String img_ul,String backdrop_path, String release_date, String overview, String original_title, int runtime, int vote_average) {
        ContentValues values = new ContentValues();
        values.put(db_helper.COLUMN_MOVIE_ID, id);
        values.put(db_helper.COLUMN_MOVIE_IMG_URL, img_ul);
        values.put(db_helper.COLUMN_MOVIE_BACKDROP_PATH, backdrop_path);
        values.put(db_helper.COLUMN_MOVIE_RELEASE_DATE, release_date);
        values.put(db_helper.COLUMN_OVERVIEW, overview);
        values.put(db_helper.COLUMN_ORIGINAL_TITLE, original_title);
        values.put(db_helper.COLUMN_RUNTIME, runtime);
        values.put(db_helper.COLUMN_VOTE_AVERAGE, vote_average);
        database.insert(db_helper.TABLE_NAME, null, values);
    }


    public Cursor get_movie(String movie_id) {
        Cursor cursor = database.query(db_helper.TABLE_NAME, allcolumns, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                if (cursor.getString(cursor.getColumnIndex(db_helper.COLUMN_MOVIE_ID)).equals(movie_id)) {
                    return cursor;
                }
            }
        }
        return null;
    }

    public ArrayList<String> get_movies_id() {
        ArrayList<String> movies_id = new ArrayList<String>();
        Cursor cursor = database.query(db_helper.TABLE_NAME, allcolumns, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                movies_id.add(cursor.getString(cursor.getColumnIndex(db_helper.COLUMN_MOVIE_ID)));
            }
        }
        return movies_id;
    }

    public ArrayList<String> get_movies_imgs_url() {
        ArrayList<String> movies_imgs_url = new ArrayList<String>();
        Cursor cursor = database.query(db_helper.TABLE_NAME, allcolumns, null, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                movies_imgs_url.add(cursor.getString(cursor.getColumnIndex(db_helper.COLUMN_MOVIE_IMG_URL)));
            }
        }
        return movies_imgs_url;
    }
}
