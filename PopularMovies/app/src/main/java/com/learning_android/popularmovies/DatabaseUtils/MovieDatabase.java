package com.learning_android.popularmovies.DatabaseUtils;


import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.learning_android.popularmovies.Objects.MovieObject;

@Database(entities = {MovieObject.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {
    private static final String TAG = "MovieDatabase";

    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "favorite_movies";
    private static MovieDatabase sInstance;

    public static MovieDatabase getInstance(Context context){
        if(sInstance==null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class, MovieDatabase.DATABASE_NAME)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        Log.d(TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract FavoriteDao favoriteDao();
}
