package com.learning_android.popularmovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.learning_android.popularmovies.Adapters.MovieAdapter;
import com.learning_android.popularmovies.DatabaseUtils.MovieDatabase;
import com.learning_android.popularmovies.Helpers.UniversalHelper;
import com.learning_android.popularmovies.NetworkUtils.MovieDB;
import com.learning_android.popularmovies.NetworkUtils.NetworkStatus;
import com.learning_android.popularmovies.NetworkUtils.ParseJSON;
import com.learning_android.popularmovies.Objects.MovieObject;
import com.learning_android.popularmovies.ViewModels.MainViewModel;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<MovieObject>>, MovieAdapter.MovieAdapterOnClickHandler {

    // RecyclerView instance;
    private RecyclerView rvMovies;
    // Current sort
    private String currentSort = MovieDB.POPULAR_TAG;
    // Current URL
    private URL currentURL = null;
    // Progress Bar
    private ProgressBar progressBar;
    // Tag for the loader
    private static final int MOVIE_LOADER_ID = 456;
    // Loader Manager
    private LoaderManager loaderManager;

    private static final String FAV_TAG = "fav_tag";
    private static final String BUNDLE_SORT = "current_sort";

    private MovieDatabase movieDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey(BUNDLE_SORT)){
                currentSort = savedInstanceState.getString(BUNDLE_SORT);
                Log.d("bundle", "Current sort now -->  "  + currentSort);
            }
        }
        // Finding the recyclerView
        rvMovies = findViewById(R.id.rv_movies);

        // Finding the progress bar
        progressBar = findViewById(R.id.pb_loading_indicator);

        movieDatabase = MovieDatabase.getInstance(this);

        loaderManager = LoaderManager.getInstance(this);
        if(currentSort.equals(FAV_TAG)){
            moviesFromViewModel();
        }
        else{
            Bundle bundle = new Bundle();
//            bundle.putString(BUNDLE_SORT, currentSort);
            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);
        }

        // Inflate the recycler view only if we have network
        if(NetworkStatus.isOnline(this)){
            // Method to inflate RecyclerView
            inflateRecyclerView();
        }
        // show the network error info
        // Also User needs to restart the app for getting the app working
        else{
            showNetworkError();
        }

    }



    // Method to show the network error
    private void showNetworkError() {
        TextView networkInfoError = findViewById(R.id.tv_network_info);
        networkInfoError.setVisibility(View.VISIBLE);
    }

    /**
     Method to set the recyclerView and inflating it
     **/
    private void inflateRecyclerView() {

        boolean shouldReverseTheLayout = false;

        // Context of this activity;
        Context context = MainActivity.this;

        // Stating the column span of the gridlayout according to the orientation of the device
        int spanCount = UniversalHelper.calculateNoOfColumns(this);

        // Instantiate the gridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, spanCount,
                LinearLayoutManager.VERTICAL,shouldReverseTheLayout);

        // set the layoutManager to recyclerView
        rvMovies.setLayoutManager(gridLayoutManager);
    }

    // Method to inflate the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.default_menu, menu);
        return true;
    }

    // If user selects one of the options in the menu then take action
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.popular_button) {
            if(currentSort.equals(MovieDB.POPULAR_TAG)){
                Toast.makeText(this, "Already sorted according to Popular Tag", Toast.LENGTH_LONG).show();
            }
            else{
                currentSort = MovieDB.POPULAR_TAG;
                loaderManager.restartLoader(MOVIE_LOADER_ID, null, this);
            }
            return true;
        }
        if(item.getItemId()==R.id.top_rated_button) {
            if(currentSort.equals(MovieDB.TOP_RATED_TAG)){
                Toast.makeText(this, "Already sorted according to Top Rated Tag", Toast.LENGTH_LONG).show();
            }
            else{
                currentSort=MovieDB.TOP_RATED_TAG;
                loaderManager.restartLoader(MOVIE_LOADER_ID, null, this);
            }
            return true;
        }

        if(item.getItemId()==R.id.menu_fav_button){
            moviesFromViewModel();
        }

        return super.onOptionsItemSelected(item);
    }

    private void moviesFromViewModel() {
        currentSort = FAV_TAG;
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getMoviesList().observe(this, new Observer<List<MovieObject>>() {
            @Override
            public void onChanged(List<MovieObject> movieObjects) {
                ArrayList<MovieObject> favMovieList = new ArrayList<>(movieObjects);
                Log.d("database", "onChanged: Updating the favMovieList");
                sendListToAdapter(favMovieList);
            }
        });
    }

    @NonNull
    @Override
    public Loader<ArrayList<MovieObject>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<ArrayList<MovieObject>>(this) {
            ArrayList<MovieObject> moviesList;
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                showLoadingBar();
                if(moviesList==null){
                    forceLoad();
                }
                else{
                    deliverResult(moviesList);
                }
            }

            @Nullable
            @Override
            public ArrayList<MovieObject> loadInBackground() {
                try{
                    // build the URL
                    currentURL = MovieDB.buildURL(currentSort);
                    // Get the response from the URL
                    String jsonResponse = MovieDB.getResponseFromHttpsUrl(currentURL);
                    // Parse the Json Response
                    moviesList = ParseJSON.parseMovie(jsonResponse);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

                return moviesList;
            }

            @Override
            public void deliverResult(@Nullable ArrayList<MovieObject> data) {
                moviesList = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<MovieObject>> loader, ArrayList<MovieObject> data) {
        hideLoadingBar();
        sendListToAdapter(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<MovieObject>> loader) {

    }

    // Method to set the adapter and send the movie list to the adapter of the recycler view
    private void sendListToAdapter(ArrayList<MovieObject> movieList) {
        MovieAdapter movieAdapter = new MovieAdapter(this, movieList,this);
        rvMovies.setAdapter(movieAdapter);
    }

    // Method to handle the clicked item of recycler view
    @Override
    public void onClick(MovieObject movieObject){
        //Create an instance and send the movieObject as intent extra
        Intent intent = new Intent(this, ShowMovieDetailsActivity.class);
        intent.putExtra("CURRENT_MOVIE", movieObject);
        startActivity(intent);
    }

    // Method to hide the progress bar
    private void hideLoadingBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    // Method to show the progress bar
    private void showLoadingBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(BUNDLE_SORT, currentSort);
        super.onSaveInstanceState(outState);
    }
}
