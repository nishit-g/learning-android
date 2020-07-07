package com.learning_android.popularmovies.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.learning_android.popularmovies.Objects.ReviewObject;
import com.learning_android.popularmovies.R;
import java.util.ArrayList;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    private Context context;
    private ArrayList<ReviewObject> reviewList;

    public ReviewAdapter(Context context, ArrayList<ReviewObject> reviewList) {
        this.context =context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_list,parent,false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        ReviewObject currentReviewObject = reviewList.get(position);

        holder.tvContent.setText(currentReviewObject.getContent());
        holder.tvAuthor.setText("-" + currentReviewObject.getAuthor());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    static class ReviewHolder extends RecyclerView.ViewHolder{
        TextView tvAuthor;
        TextView tvContent;

        ReviewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tv_review_author);
            tvContent = itemView.findViewById(R.id.tv_review_content);
        }
    }
}
