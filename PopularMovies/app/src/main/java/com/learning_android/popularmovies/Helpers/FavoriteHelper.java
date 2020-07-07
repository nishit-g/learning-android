package com.learning_android.popularmovies.Helpers;

import android.util.Log;
import com.learning_android.popularmovies.DatabaseUtils.AppExecutor;
import com.learning_android.popularmovies.DatabaseUtils.MovieDatabase;
import com.learning_android.popularmovies.Objects.MovieObject;

public class FavoriteHelper {
    private FavoriteHelper(){
        // Prevent Instantiation
    }

    public static void removeFromFavorites(AppExecutor executor, final MovieDatabase database, final MovieObject movie){
        movie.setFavorite(false);
        executor.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.favoriteDao().removeMovie(movie);
            }
        });
        Log.d("database", "removeFromFavorites: " + movie.getOriginalTitle());
    }

    public static void addToFavorites(AppExecutor executor, final MovieDatabase database, final MovieObject movie){
        movie.setFavorite(true);
        executor.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                database.favoriteDao().addMovieToDatabase(movie);
            }
        });
        Log.d("database", "addToFavorites: " + movie.getOriginalTitle());
    }
}
