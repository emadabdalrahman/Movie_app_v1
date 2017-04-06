package com.example.android.movie_app;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


public class movie_details_activity extends AppCompatActivity {

    public String movie_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String state = getIntent().getExtras().getString("state");
        movie_id = getIntent().getExtras().getString("movie_id");
        if (isOnline()||state.equals("favorite")){
            setContentView(R.layout.activity_movie_details);

            final Toolbar toolbar = (Toolbar) findViewById(R.id.movie_details_toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            movie_details_fragment movieDetailsFragment = new movie_details_fragment();
            Bundle bundle = new Bundle();
            bundle.putString("movie_id",movie_id);
            bundle.putString("state",state);
            movieDetailsFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_movie_details,movieDetailsFragment).commit();
        }else{
            Intent intent = new Intent(this, no_internet_connection.class);
            intent.putExtra("called_from","movie_details");
            intent.putExtra("movie_id",movie_id);
            intent.putExtra("state",state);
            startActivity(intent);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
