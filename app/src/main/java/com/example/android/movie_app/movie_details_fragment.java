package com.example.android.movie_app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by emad on 11/16/2016.
 */

public class movie_details_fragment extends Fragment {

    public String movie_img_url;
    public String movie_id;
    public String release_date;
    public String overview;
    public String original_title;
    public String backdrop_path;
    public int vote_average;
    public int runtime;
    public FragmentActivity mContext;
    public ImageView movie_poster;
    public TextView release_date_tv;
    public TextView runtime_tv;
    public RatingBar ratingBar;
    public TextView vote_average_tv;
    public TextView overview_tv;
    public TextView original_title_tv;
    public ImageView background_img;
    public LinearLayout review_layout;
    public LinearLayout trailer_layout;
    public ImageButton favorite_button;
    public String state;
    public View line_view1;
    public View line_view2;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (FragmentActivity) context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root_view = inflater.inflate(R.layout.fragment_movie_details, container, false);

        movie_poster = (ImageView) root_view.findViewById(R.id.movie_poster);
        release_date_tv = (TextView) root_view.findViewById(R.id.release_date);
        runtime_tv = (TextView) root_view.findViewById(R.id.runtime);
        ratingBar = (RatingBar) root_view.findViewById(R.id.ratingBar);
        vote_average_tv = (TextView) root_view.findViewById(R.id.vote_average);
        overview_tv = (TextView) root_view.findViewById(R.id.overview);
        original_title_tv = (TextView) root_view.findViewById(R.id.original_title);
        background_img = (ImageView) root_view.findViewById(R.id.movie_details_background_img);
        review_layout = (LinearLayout) root_view.findViewById(R.id.reviews);
        trailer_layout = (LinearLayout) root_view.findViewById(R.id.trailer);
        favorite_button = (ImageButton) root_view.findViewById(R.id.favorite_b);

        line_view1 = (View) root_view.findViewById(R.id.line_view);
        line_view2 = (View) root_view.findViewById(R.id.line_view2);

        movie_details_invisible();
        movie_id = getArguments().getString("movie_id");
        state = getArguments().getString("state");
        final String movie_details_url = "https://api.themoviedb.org/3/movie/" + movie_id + "?api_key=c8e1ac8910c753600986ed6d71f88e29&language=en-US";
        final String movie_review_url = "https://api.themoviedb.org/3/movie/" + movie_id + "/reviews?api_key=c8e1ac8910c753600986ed6d71f88e29&language=en-US";
        final String movie_videos_url = "https://api.themoviedb.org/3/movie/" + movie_id + "/videos?api_key=c8e1ac8910c753600986ed6d71f88e29&language=en-US";


        makeAsFavorite_listener();
        if (Is_Favorite()) {
            favorite_button.setImageResource(R.drawable.heart_icon);
            favorite_button.setEnabled(false);
        }

        if (state.equals("favorite")) {
            get_movie_data_from_db();
        } else {
            start_connection_task_movie_details(movie_details_url);
            start_connection_task_videos(movie_videos_url);
            start_connection_task_review(movie_review_url);
        }
        return root_view;
    }

    public void movie_details_invisible() {
        movie_poster.setVisibility(View.INVISIBLE);
        release_date_tv.setVisibility(View.INVISIBLE);
        runtime_tv.setVisibility(View.INVISIBLE);
        ratingBar.setVisibility(View.INVISIBLE);
        vote_average_tv.setVisibility(View.INVISIBLE);
        overview_tv.setVisibility(View.INVISIBLE);
        original_title_tv.setVisibility(View.INVISIBLE);
        background_img.setVisibility(View.INVISIBLE);
        review_layout.setVisibility(View.INVISIBLE);
        trailer_layout.setVisibility(View.INVISIBLE);
        favorite_button.setVisibility(View.INVISIBLE);
        line_view1.setVisibility(View.INVISIBLE);
        line_view2.setVisibility(View.INVISIBLE);

    }

    public void get_movie_data_from_db() {
        db_control dbControl = new db_control(mContext);
        dbControl.open_db();
        Cursor cursor = dbControl.get_movie(movie_id);
        movie_img_url = cursor.getString(cursor.getColumnIndex(db_helper.COLUMN_MOVIE_IMG_URL));
        original_title = cursor.getString(cursor.getColumnIndex(db_helper.COLUMN_ORIGINAL_TITLE));
        overview = cursor.getString(cursor.getColumnIndex(db_helper.COLUMN_OVERVIEW));
        release_date = cursor.getString(cursor.getColumnIndex(db_helper.COLUMN_MOVIE_RELEASE_DATE));
        vote_average = cursor.getInt(cursor.getColumnIndex(db_helper.COLUMN_VOTE_AVERAGE));
        runtime = cursor.getInt(cursor.getColumnIndex(db_helper.COLUMN_RUNTIME));
        backdrop_path = cursor.getString(cursor.getColumnIndex(db_helper.COLUMN_MOVIE_BACKDROP_PATH));
        dbControl.close_db();

        Picasso.with(mContext)
                .load(movie_img_url)
                .into(movie_poster);
        movie_poster.setVisibility(View.VISIBLE);

        Picasso.with(mContext)
                .load(backdrop_path)
                .into(background_img);
        background_img.setVisibility(View.VISIBLE);

        release_date_tv.setText(release_date);
        release_date_tv.setVisibility(View.VISIBLE);

        double ratingBar_value = vote_average / 2.0;
        ratingBar.setRating(((float) ratingBar_value));
        ratingBar.setVisibility(View.VISIBLE);
        vote_average_tv.setText("" + vote_average + "/10");
        vote_average_tv.setVisibility(View.VISIBLE);

        runtime_tv.setText("" + runtime + " m");
        runtime_tv.setVisibility(View.VISIBLE);

        overview_tv.setText("Overview : " + overview);
        overview_tv.setVisibility(View.VISIBLE);

        original_title_tv.setText(original_title);
        original_title_tv.setVisibility(View.VISIBLE);

        favorite_button.setVisibility(View.VISIBLE);

        line_view2.setVisibility(View.INVISIBLE);
    }

    public boolean Is_Favorite() {
        ArrayList<String> movies_id = new ArrayList<String>();
        db_control db_control = new db_control(mContext);
        db_control.open_db();
        movies_id = db_control.get_movies_id();
        for (int i = 0; i < movies_id.size(); i++) {
            if (movies_id.get(i).equals(movie_id)) {
                return true;
            }
        }
        return false;
    }

    public void start_connection_task_movie_details(String movie_details_url) {
        Connection_task connection_task = new Connection_task() {
            @Override
            protected void onPostExecute(String res) {
                try {
                    JSONObject json_root = new JSONObject(res);
                    String poster_path = json_root.getString("poster_path");
                    String total_img_url = "http://image.tmdb.org/t/p/w342" + poster_path;
                    movie_img_url = total_img_url;

                    Picasso.with(mContext)
                            .load(total_img_url)
                            .into(movie_poster);
                    movie_poster.setVisibility(View.VISIBLE);

                    release_date = json_root.getString("release_date");
                    release_date_tv.setText(release_date);
                    release_date_tv.setVisibility(View.VISIBLE);

                    runtime = json_root.getInt("runtime");
                    runtime_tv.setText("" + runtime + " m");
                    runtime_tv.setVisibility(View.VISIBLE);

                    vote_average = json_root.getInt("vote_average");
                    double ratingBar_value = vote_average / 2.0;
                    ratingBar.setRating(((float) ratingBar_value));
                    ratingBar.setVisibility(View.VISIBLE);
                    vote_average_tv.setText("" + vote_average + "/10");
                    vote_average_tv.setVisibility(View.VISIBLE);

                    overview = json_root.getString("overview");
                    overview_tv.setText("Overview : " + overview);
                    overview_tv.setVisibility(View.VISIBLE);

                    original_title = json_root.getString("original_title");
                    original_title_tv.setText(original_title);
                    original_title_tv.setVisibility(View.VISIBLE);

                    backdrop_path = "http://image.tmdb.org/t/p/w342" + json_root.getString("backdrop_path");
                    Picasso.with(mContext)
                            .load(backdrop_path)
                            .into(background_img);
                    background_img.setVisibility(View.VISIBLE);

                    line_view1.setVisibility(View.VISIBLE);
                    favorite_button.setVisibility(View.VISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        connection_task.execute(movie_details_url);
    }

    public void start_connection_task_review(String movie_review_url) {
        Connection_task connection_task_review = new Connection_task() {
            String content = null;
            String author = null;

            @Override
            protected void onPostExecute(String res) {
                try {
                    JSONObject root = new JSONObject(res);
                    JSONArray results = root.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject review_data = results.getJSONObject(i);
                        author = review_data.getString("author");
                        content = review_data.getString("content");

                        TextView title = new TextView(mContext);
                        TextView sub_title = new TextView(mContext);


                        title.setText("Author : " + author);
                        title.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        title.setTextColor(Color.parseColor("#FFFFFF"));

                        sub_title.setText("Content : " + content + "\n");
                        sub_title.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        sub_title.setTextColor(Color.parseColor("#FFFFFF"));

                        review_layout.addView(title);
                        review_layout.addView(sub_title);
                        review_layout.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        connection_task_review.execute(movie_review_url);

    }

    public void start_connection_task_videos(String movie_videos_url) {
        Connection_task connection_task = new Connection_task() {
            String name;
            String trailer_url;

            @Override
            protected void onPostExecute(String res) {
                try {
                    JSONObject root = new JSONObject(res);
                    JSONArray results = root.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject data = results.getJSONObject(i);
                        name = data.getString("name");
                        trailer_url = "https://www.youtube.com/watch?v=" + data.getString("key");
                        TextView text = new TextView(mContext);


                        text.setText(name + " \n");
                        text.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        text.setTextColor(Color.parseColor("#ea4040"));
                        text.setTextSize(15);
                        text.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer_url));
                                mContext.startActivity(intent);
                            }
                        });


                        trailer_layout.addView(text);
                        trailer_layout.setVisibility(View.VISIBLE);

                        line_view2.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        connection_task.execute(movie_videos_url);
    }

    public void makeAsFavorite_listener() {
        favorite_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db_control db_control = new db_control(mContext);
                db_control.open_db();
                db_control.insert_movie(movie_id, movie_img_url, backdrop_path, release_date, overview, original_title, runtime, vote_average);
                db_control.close_db();
                favorite_button.setImageResource(R.drawable.heart_icon);
                favorite_button.setEnabled(false);
            }
        });
    }

}
