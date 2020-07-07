package com.learning_android.popularmovies.NetworkUtils;

import com.learning_android.popularmovies.Objects.MovieObject;
import com.learning_android.popularmovies.Objects.ReviewObject;
import com.learning_android.popularmovies.Objects.TrailerObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class ParseJSON {

    public static ArrayList<MovieObject> parseMovie(String jsonResponse) throws JSONException {
        ArrayList<MovieObject> movieList = new ArrayList<>();

        try{

            JSONObject jsonMovieObject = new JSONObject(jsonResponse);

            JSONArray jsonMovieArray = jsonMovieObject.getJSONArray("results");
            for(int i =0; i<jsonMovieArray.length(); i++){
                MovieObject currentMovieObject = new MovieObject();

                JSONObject currentJsonMovie = (JSONObject) jsonMovieArray.get(i);

                String movieTitle = currentJsonMovie.getString("title");
                currentMovieObject.setOriginalTitle(movieTitle);

                String overview =  currentJsonMovie.getString("overview");
                currentMovieObject.setOverview(overview);

                String posterPath = currentJsonMovie.getString("poster_path");
                currentMovieObject.setPosterPath(posterPath);

                String releaseDate = currentJsonMovie.getString("release_date");
                currentMovieObject.setReleaseDate(releaseDate);

                Double voteAverage = currentJsonMovie.getDouble("vote_average");
                currentMovieObject.setVoteAverage(voteAverage);

                int movieID = currentJsonMovie.getInt("id");
                currentMovieObject.setMovieId(movieID);

                currentMovieObject.setFavorite(false);

                movieList.add(currentMovieObject);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  movieList;
    }

    public static ArrayList<ReviewObject> parseReview(String jsonResponse) throws  JSONException{
        ArrayList<ReviewObject> reviewList = new ArrayList<>();

        try{
            JSONObject reviewObject = new JSONObject(jsonResponse);
            JSONArray jsonReviewList = reviewObject.getJSONArray("results");
            for(int i=0; i<jsonReviewList.length(); i++){
                ReviewObject currentReviewObject = new ReviewObject();
                JSONObject currentJsonReview = (JSONObject) jsonReviewList.get(i);

                String author = currentJsonReview.getString("author");
                currentReviewObject.setAuthor(author);

                String content = currentJsonReview.getString("content");
                currentReviewObject.setContent(content);

                reviewList.add(currentReviewObject);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return reviewList;
    }

    public static ArrayList<TrailerObject> parseTrailer(String jsonResponse) throws JSONException{
        ArrayList<TrailerObject> trailerList = new ArrayList<>();
        try{
            JSONObject trailerObject = new JSONObject(jsonResponse);
            JSONArray jsonTrailerList = trailerObject.getJSONArray("results");
            for(int i =0 ; i<jsonTrailerList.length(); i++){
                TrailerObject cTrailerObject = new TrailerObject();
                JSONObject cJsonObject = (JSONObject) jsonTrailerList.get(i);

                String videoKey = cJsonObject.getString("key");
                cTrailerObject.setVideoKey(videoKey);
                trailerList.add(cTrailerObject);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return trailerList;
    }

}
