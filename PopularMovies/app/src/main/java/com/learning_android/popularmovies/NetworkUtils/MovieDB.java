package com.learning_android.popularmovies.NetworkUtils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public final class MovieDB {
    private static final String TAG = "MovieDB";

    // Base Url
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    // Path Parameters
    public static final String POPULAR_TAG = "popular";
    public static final String TOP_RATED_TAG = "top_rated";
    public static final String REVIEW_TAG = "reviews";
    public static final String TRAILER_TAG = "videos";

    // Query Parameters
    private static final String API_PARAM = "api_key";

    // TODO : INSERT YOUR API KEY
    private static final String API_KEY = "";


    // Method to build the URL according to the sorting tag BY DEFAULT--> POPULAR TAG
    public static URL buildURL(String movieOrder){
        Log.d(TAG, "buildURL: Building Url with tag : " + movieOrder);
            Uri builtUri = Uri.parse(BASE_URL)
                    .buildUpon()
                    .appendPath(movieOrder)
                    .appendQueryParameter(API_PARAM, API_KEY)
                    .build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // For knowing if the URL is correct or wrong
        Log.d(TAG, "buildURL: URL : " + url);
        return url;
    }


    public static URL buildURL(int movieId, String tag){
        Uri builtUri = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath(String.valueOf(movieId))
                .appendPath(tag)
                .appendQueryParameter(API_PARAM, API_KEY)
                .build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // For knowing if the URL is correct or wrong
        Log.d(TAG, "buildURL: Review URL : " + url);
        return url;
    }

    // Method for getting the content from the URL
    public static String getResponseFromHttpsUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        InputStream in = urlConnection.getInputStream();
        Scanner scanner = new Scanner(in);
        try {
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                scanner.close();
                return null;
            }
        }finally{
            scanner.close();
            urlConnection.disconnect();
        }
    }
}
