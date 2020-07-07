package com.learning_android.popularmovies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.learning_android.popularmovies.Objects.MovieObject;
import com.learning_android.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter  extends  RecyclerView.Adapter<MovieAdapter.MovieViewHolder>{
    private  ArrayList<MovieObject> movieList;
    private Context context;
    private final MovieAdapterOnClickHandler movieAdapterOnClickHandler;

    public interface MovieAdapterOnClickHandler{
        void onClick(MovieObject movieObject);
    }

    public MovieAdapter(Context context, ArrayList<MovieObject> movieList, MovieAdapterOnClickHandler movieAdapterOnClickHandler) {
        this.context = context;
        this.movieList = movieList;
        this.movieAdapterOnClickHandler = movieAdapterOnClickHandler;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_list,parent,false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieObject currentMovieObject =  movieList.get(position);
        String title = currentMovieObject.getOriginalTitle();
        holder.tvMovieTitle.setText(title);

        String posterUrl = "https://image.tmdb.org/t/p/w185/" + currentMovieObject.getPosterPath();
        Picasso.get()
                .load(posterUrl)
                .into(holder.ivMoviePoster);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvMovieTitle;
        ImageView ivMoviePoster;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMovieTitle = itemView.findViewById(R.id.tv_movie_title);
            ivMoviePoster = itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            movieAdapterOnClickHandler.onClick(movieList.get(adapterPosition));
        }
    }
}
