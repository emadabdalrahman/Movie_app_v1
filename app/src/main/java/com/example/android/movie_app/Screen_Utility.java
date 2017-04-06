package com.example.android.movie_app;

import android.app.Activity;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * Created by emad on 11/12/2016.
 */

public class Screen_Utility {
    private double width_dp;
    private double height_dp;
    private Activity activity;

    public Screen_Utility(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
//        Point point = new Point();
//        display.getSize(point);
//        height_dp = point.y;
//        width_dp=point.x;


        DisplayMetrics displayMetrics =new DisplayMetrics();
        display.getMetrics(displayMetrics);

        Float density = activity.getResources().getDisplayMetrics().density;
        height_dp = displayMetrics.heightPixels/density;
        width_dp = displayMetrics.widthPixels/density;

    }
    public double getWidth_dp(){return width_dp;}
    public double getHeight_dp(){return height_dp;}
}
