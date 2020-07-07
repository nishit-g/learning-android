package com.learning_android.popularmovies.Helpers;

import android.content.Context;
import android.util.DisplayMetrics;

public class UniversalHelper {
    // Method to get the Number of columns
    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth/scalingFactor);
        if(noOfColumns<2)
            noOfColumns = 2;
        return noOfColumns;
    }
}
