package com.example.android.movie_app;

import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by emad on 10/29/2016.
 */

public class Connection_task extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... url) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url[0])
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}