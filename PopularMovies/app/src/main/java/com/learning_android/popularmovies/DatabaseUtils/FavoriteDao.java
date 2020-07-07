package com.learning_android.popularmovies.DatabaseUtils;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.learning_android.popularmovies.Objects.MovieObject;

import java.util.List;

@Dao
public interface FavoriteDao {
    @Query("SELECT * FROM favorite_movies ORDER BY voteAverage DESC")
    LiveData<List<MovieObject>> loadFavMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addMovieToDatabase(MovieObject movieObject);

    @Update
    void updateMovie(MovieObject movieObject);

    @Delete
    void removeMovie(MovieObject movieObject);

    @Query("SELECT isFavorite FROM favorite_movies WHERE movieId = :movieId")
    boolean isMovieFavorite(int movieId);
}
