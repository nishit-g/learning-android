package com.learning_android.popularmovies.NetworkUtils;

import android.content.Context;
import android.net.ConnectivityManager;

// Helper Class for knowing the status of the internet connectivity

public class NetworkStatus {

    // This method seemed easy to implement even if the methods are deprecated
    // Used stack overflow's answer

    public static boolean isOnline(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo()!=null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
