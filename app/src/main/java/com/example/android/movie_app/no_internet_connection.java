package com.example.android.movie_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class no_internet_connection extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet_connection);
    }

    public void refresh_internet(View view) {

        if(isOnline()){
            String called_from = getIntent().getExtras().getString("called_from");
            if (called_from.equals("main")) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            if (called_from.equals("movie_details")){
                String state = getIntent().getExtras().getString("state");
                String movie_id = getIntent().getExtras().getString("movie_id");
                Intent intent = new Intent(this, movie_details_activity.class);
                intent.putExtra("movie_id",movie_id);
                intent.putExtra("state",state);
                startActivity(intent);
            }
        }
    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
