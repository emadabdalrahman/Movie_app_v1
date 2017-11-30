package com.example.android.movie_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.android.movie_app.MainActivity.API_KEY;


public class main_fragment extends Fragment {

    public String state;
    public onclick_listener listener;
    public GridView gridView;
    public String request;
    public FragmentActivity mContext;

    public String popular_url = "https://api.themoviedb.org/3/movie/popular?api_key="+API_KEY+"&language=en-US&page=1";
    public String top_rated_url = "https://api.themoviedb.org/3/movie/top_rated?page=1&language=en-US&api_key="+API_KEY;
    public SharedPreferences setting;

    static interface onclick_listener {
        void set_request(String res);

        void grid_view_onclick_listener(GridView gridView);

        void toolbar_listener(String title);

        boolean isOnline();

        SharedPreferences get_SharedPreferences(String name, int mode);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (onclick_listener) getActivity();
        mContext = (FragmentActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View root_view = null;
        root_view = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) root_view.findViewById(R.id.gridView);
        setting = listener.get_SharedPreferences("setting", MODE_PRIVATE);
        state = setting.getString("state", "popular");
        if (state.equals("popular") && listener.isOnline()) {
            start_connection_task(popular_url);
        } else if (state.equals("top_rated") && listener.isOnline()) {
            start_connection_task(top_rated_url);
        } else if (state.equals("favorite")) {
            get_favorite_movies();
        } else {
            Intent intent = new Intent(mContext, no_internet_connection.class);
            intent.putExtra("called_from","main");
            startActivity(intent);
        }
        listener.grid_view_onclick_listener(gridView);
        setHasOptionsMenu(true);
        return root_view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.movies_type_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String state;
        switch (item.getItemId()) {
            case R.id.popular_m:
                if (listener.isOnline()) {
                    start_connection_task(popular_url);
                    listener.toolbar_listener("popular");
                    return true;
                } else {
                    Intent intent = new Intent(mContext, no_internet_connection.class);
                    intent.putExtra("called_from","main");
                    startActivity(intent);
                    return false;
                }
            case R.id.top_rated_m:
                if (listener.isOnline()) {
                    start_connection_task(top_rated_url);
                    listener.toolbar_listener("top_rated");
                    return true;
                } else {
                    Intent intent = new Intent(mContext, no_internet_connection.class);
                    intent.putExtra("called_from","main");
                    startActivity(intent);
                    return false;
                }
            case R.id.favorite_m:
                get_favorite_movies();
                listener.toolbar_listener("favorite");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void start_connection_task(String url) {

        Connection_task connection_task = new Connection_task() {
            @Override
            protected void onPostExecute(String res) {
                try {

                    ArrayList<String> imgs_url = new ArrayList<String>();
                    listener.set_request(res);
                    JSONObject json_root = new JSONObject(res);
                    final JSONArray jsonArray = json_root.getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject json_movie_Object = jsonArray.getJSONObject(i);
                        String poster_path = json_movie_Object.getString("poster_path");
                        String total_img_url = "http://image.tmdb.org/t/p/w342" + poster_path;
                        imgs_url.add(total_img_url);
                    }
                    Display display = mContext.getWindowManager().getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int height = ((point.y) / 2);


                    custom_grid_adapter custom_grid_adapter = new custom_grid_adapter(mContext, imgs_url, height);
                    gridView.setAdapter(custom_grid_adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        connection_task.execute(url);
    }

    public void get_favorite_movies() {
        ArrayList<String> movies_imgs_url = new ArrayList<String>();
        db_control db_control = new db_control(mContext);
        db_control.open_db();
        movies_imgs_url = db_control.get_movies_imgs_url();
        db_control.close_db();

        Display display = mContext.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int height = ((point.y) / 2);

        custom_grid_adapter custom_grid_adapter = new custom_grid_adapter(mContext, movies_imgs_url, height);
        gridView.setAdapter(custom_grid_adapter);
    }


}
