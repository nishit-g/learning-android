package com.learning_android.popularmovies.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.learning_android.popularmovies.DatabaseUtils.MovieDatabase;
import com.learning_android.popularmovies.Objects.MovieObject;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<MovieObject>> moviesList;

    public MainViewModel(@NonNull Application application) {
        super(application);
        MovieDatabase database = MovieDatabase.getInstance(this.getApplication());
        moviesList = database.favoriteDao().loadFavMovies();
    }

    public LiveData<List<MovieObject>> getMoviesList() {
        return moviesList;
    }
}
