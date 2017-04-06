package com.example.android.movie_app;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class custom_grid_adapter extends BaseAdapter {

    int height;
    ArrayList<String> imgs_url = new ArrayList<String>();
    Context context;

    public custom_grid_adapter(Context context, ArrayList<String> imgs_url, int height) {

        this.height = height;
        this.imgs_url = imgs_url;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imgs_url.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(GridLayout.LayoutParams.MATCH_PARENT, height));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(context)
                .load(imgs_url.get(position))
                .into(imageView);
        return imageView;
    }
}
