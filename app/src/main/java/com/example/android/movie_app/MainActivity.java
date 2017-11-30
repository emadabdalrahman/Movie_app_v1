package com.example.android.movie_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements main_fragment.onclick_listener {
    public static final String API_KEY = "";
    public String request, state;
    public JSONArray jsonArray;
    public String popular_url = "https://api.themoviedb.org/3/movie/popular?api_key="+API_KEY+"&language=en-US&page=1";
    public String top_rated_url = "https://api.themoviedb.org/3/movie/top_rated?page=1&language=en-US&api_key="+API_KEY;
    SharedPreferences setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setting = getSharedPreferences("setting", MODE_PRIVATE);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mainactivity_toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void grid_view_onclick_listener(GridView gridView) {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Screen_Utility screen_utility = new Screen_Utility(MainActivity.this);
                if (screen_utility.getWidth_dp() >= 500) {
                    tablet_mode_grid_on_click_listener(position);
                } else {
                    normal_mode_grid_on_click_listener(position);
                }
            }
        });
    }

    @Override
    public void toolbar_listener(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.mainactivity_toolbar);
        toolbar.setTitle(title);
        state = title;
        SharedPreferences.Editor editor = setting.edit();
        editor.putString("state", state);
        editor.apply();
    }

    @Override
    public void set_request(String res) {
        this.request = res;
    }

    public void tablet_mode_grid_on_click_listener(int position) {
        state = setting.getString("state", "popular");
            if (state.equals("favorite")) {
                ArrayList<String> movies_id = new ArrayList<String>();
                db_control db_control = new db_control(MainActivity.this);
                db_control.open_db();
                movies_id = db_control.get_movies_id();
                Bundle bundle = new Bundle();
                bundle.putString("movie_id", movies_id.get(position));
                bundle.putString("state", state);
                movie_details_fragment movieDetailsFragment = new movie_details_fragment();
                movieDetailsFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, movieDetailsFragment).commit();
            } else if (state.equals("top_rated") || state.equals("popular") && isOnline()) {
                JSONObject json_movie_Object = null;
                try {
                    JSONObject jsonObject = new JSONObject(request);
                    jsonArray = jsonObject.getJSONArray("results");
                    json_movie_Object = jsonArray.getJSONObject(position);
                    int movie_id = json_movie_Object.getInt("id");
                    Bundle bundle = new Bundle();
                    bundle.putString("movie_id", "" + movie_id + "");
                    bundle.putString("state", state);
                    movie_details_fragment movieDetailsFragment = new movie_details_fragment();
                    movieDetailsFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, movieDetailsFragment).commit();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
            Intent intent = new Intent(this, no_internet_connection.class);
            intent.putExtra("called_from","main");
            startActivity(intent);
        }

    }

    public void normal_mode_grid_on_click_listener(int position) {
        state = setting.getString("state", "popular");
        if (state.equals("favorite")) {
            ArrayList<String> movies_id = new ArrayList<String>();
            Intent intent = new Intent(MainActivity.this, movie_details_activity.class);
            db_control db_control = new db_control(MainActivity.this);
            db_control.open_db();
            movies_id = db_control.get_movies_id();
            intent.putExtra("movie_id", movies_id.get(position));
            intent.putExtra("state", state);
            startActivity(intent);
        } else if (state.equals("top_rated") || state.equals("popular")) {
            Intent intent = new Intent(MainActivity.this, movie_details_activity.class);
            JSONObject json_movie_Object = null;
            try {
                JSONObject jsonObject = new JSONObject(request);
                jsonArray = jsonObject.getJSONArray("results");
                json_movie_Object = jsonArray.getJSONObject(position);
                int movie_id = json_movie_Object.getInt("id");
                intent.putExtra("movie_id", "" + movie_id + "");
                intent.putExtra("state", state);
                startActivity(intent);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public SharedPreferences get_SharedPreferences(String name, int mode) {
        SharedPreferences sharedPreferences = getSharedPreferences(name, mode);
        return sharedPreferences;
    }
}





