package com.learning_android.popularmovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.learning_android.popularmovies.Adapters.ReviewAdapter;
import com.learning_android.popularmovies.Adapters.TrailerAdapter;
import com.learning_android.popularmovies.DatabaseUtils.AppExecutor;
import com.learning_android.popularmovies.DatabaseUtils.MovieDatabase;
import com.learning_android.popularmovies.Helpers.FavoriteHelper;
import com.learning_android.popularmovies.Helpers.UniversalHelper;
import com.learning_android.popularmovies.NetworkUtils.MovieDB;
import com.learning_android.popularmovies.NetworkUtils.ParseJSON;
import com.learning_android.popularmovies.Objects.MovieObject;
import com.learning_android.popularmovies.Objects.ReviewObject;
import com.learning_android.popularmovies.Objects.TrailerObject;
import com.squareup.picasso.Picasso;
import org.json.JSONException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class ShowMovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<String>>, TrailerAdapter.ItemClickListener {

    private static final String TAG = "ShowMovieDetailActivity";
    // Variable to store the Movie data
    private MovieObject currentMovie = null;
    private static final int Review_Loader_Id = 143;
    private int movieID;

    // Variables for Review data
    private RecyclerView rvReviews;

    // Variable(s) for Trailer Data;
    private RecyclerView rvTrailer;

    private TextView tvMultiPurpose;
    private TextView noTrailer;
    private TextView tvFavText;

    private AppExecutor appExecutor;
    private MovieDatabase database;
    private ImageView ivFav;
    private boolean isFav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_movie_details);
        // Show back button
        ActionBar actionBar =getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Get the currentMovie from the intent
        currentMovie = getIntent().getParcelableExtra("CURRENT_MOVIE");
        rvReviews = findViewById(R.id.rv_movie_reviews);
        rvTrailer = findViewById(R.id.rv_trailer);
        ivFav = findViewById(R.id.iv_fav_icon);
        tvMultiPurpose  = findViewById(R.id.tv_no_connection);
        noTrailer = findViewById(R.id.tv_no_trailer);
        tvFavText = findViewById(R.id.tv_fav_fixed);

        initLoader();
        setUpLayout();
        database = MovieDatabase.getInstance(this);
        appExecutor = AppExecutor.getInstance();

        isFav = database.favoriteDao().isMovieFavorite(currentMovie.getMovieId());
        updateFavIcon(isFav);

    }

    // Method to update the Fav Icon
    private void updateFavIcon(final boolean isFav) {
        appExecutor.getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (isFav) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ivFav.setImageResource(R.drawable.heart_filled);
                            tvFavText.setText(R.string.remove_from_fav);
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvFavText.setText(R.string.add_fav);
                            ivFav.setImageResource(R.drawable.heart);
                        }
                    });
                }
            }
        });
    }

    private void setUpLayout(){
        // Set the title of the toolbar to Movie Title
        setTitle(currentMovie.getOriginalTitle());

        // Find all the needed placeholders
        ImageView moviePoster = findViewById(R.id.iv_movie_poster);
        TextView tvMovieTitle = findViewById(R.id.tv_movie_title);
        TextView tvMovieAverage = findViewById(R.id.tv_movie_average);
        TextView tvMoviePlot = findViewById(R.id.tv_movie_plot_synopsis);
        TextView tvReleaseDate = findViewById(R.id.tv_movie_release_date);

        // Get the url of the poster Image
        String posterUrl = "https://image.tmdb.org/t/p/w185/" + currentMovie.getPosterPath();

        // Set the placeholder's value
        Picasso.get()
                .load(posterUrl)
                .into(moviePoster);

        tvMovieAverage.setText(currentMovie.getVoteAverage().toString());
        tvMovieTitle.setText(currentMovie.getOriginalTitle());

        String s = "Released On : " + currentMovie.getReleaseDate();
        tvReleaseDate.setText(s);
        tvMoviePlot.setText(currentMovie.getOverview());

        movieID = currentMovie.getMovieId();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void initLoader() {
        // Using Loader for using AsyncTaskLoader and caching
        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(Review_Loader_Id,null,this);
    }

    @NonNull
    @Override
    public Loader<ArrayList<String>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<ArrayList<String>>(this) {
            // Array to store the responses : reviewJson and TrailerJson
            ArrayList<String> responseList = new ArrayList<>();
            String reviewJson;
            String trailerJson ;
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if(responseList.isEmpty()){
                    tvMultiPurpose.setText(getResources().getText(R.string.wait_for_reviews));
                    tvMultiPurpose.setVisibility(View.VISIBLE);
                    forceLoad();
                }
                else{
                    responseList.add(reviewJson);
                    responseList.add(trailerJson);
                    deliverResult(responseList);
                }
            }

            @Nullable
            @Override
            public ArrayList<String> loadInBackground() {
                Log.d(TAG, "loadInBackground: Fetching reviews and Trailers...");
                URL reviewUrl = MovieDB.buildURL(movieID,MovieDB.REVIEW_TAG);
                URL trailerUrl = MovieDB.buildURL(movieID, MovieDB.TRAILER_TAG);
                try {
                    reviewJson = MovieDB.getResponseFromHttpsUrl(reviewUrl);
                    trailerJson = MovieDB.getResponseFromHttpsUrl(trailerUrl);
                    responseList.add(reviewJson);
                    responseList.add(trailerJson);
                    return responseList;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(@Nullable ArrayList<String> data) {
                responseList = data;
                tvMultiPurpose.setVisibility(View.GONE);
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<String>> loader, ArrayList<String> data) {
        ArrayList<ReviewObject> reviewList = new ArrayList<>();
        ArrayList<TrailerObject> trailerList = new ArrayList<>();
        try {
            reviewList = ParseJSON.parseReview(data.get(0));
            trailerList = ParseJSON.parseTrailer(data.get(1));
            if(reviewList.size()==0){
                showNoReviews();
                if(trailerList.size() ==0){
                    noTrailer.setVisibility(View.VISIBLE);
                    rvTrailer.setVisibility(View.GONE);
                    return;
                }
                return;
            }
            if(trailerList.size() ==0){
                noTrailer.setVisibility(View.VISIBLE);
                rvTrailer.setVisibility(View.GONE);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initReviewAdapter(reviewList);
        initTrailerAdapter(trailerList);
    }

    private void initTrailerAdapter(ArrayList<TrailerObject> trailerList) {
        try{
            // Getting the number of columns for Grid Layout
            int spanCount = UniversalHelper.calculateNoOfColumns(this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount, LinearLayoutManager.VERTICAL,
                    false);
            rvTrailer.setLayoutManager(gridLayoutManager);

            // Initializing the adapter
            TrailerAdapter trailerAdapter = new TrailerAdapter(this,trailerList,this);
            rvTrailer.setAdapter(trailerAdapter);

            // Once everything is loaded make it Visible
            rvTrailer.setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showNoReviews() {
        tvMultiPurpose.setText(getResources().getText(R.string.no_reviews_available));
        tvMultiPurpose.setVisibility(View.VISIBLE);
        rvReviews.setVisibility(View.GONE);
    }

    private void initReviewAdapter(ArrayList<ReviewObject> reviewList) {
        try{
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
            rvReviews.setLayoutManager(linearLayoutManager);
            ReviewAdapter reviewAdapter = new ReviewAdapter(this, reviewList);
            rvReviews.setAdapter(reviewAdapter);
            rvReviews.setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<String>> loader) {

    }

    @Override
    public void onItemClickListener(int clickedItemListener, String videoKey) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoKey));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + videoKey));

        try{
            this.startActivity(appIntent);
        }catch (ActivityNotFoundException e){
            this.startActivity(webIntent);
        }
    }

    public void fav_button_clicked(View view) {

        if(isFav){
            tvFavText.setText(R.string.remove_from_fav);
            FavoriteHelper.removeFromFavorites(appExecutor,database,currentMovie);
            isFav = !isFav;
            updateFavIcon(isFav);
        }
        else{
            tvFavText.setText(R.string.add_fav);
            FavoriteHelper.addToFavorites(appExecutor,database,currentMovie);
            isFav = !isFav;
            updateFavIcon(isFav);
        }
    }
}
