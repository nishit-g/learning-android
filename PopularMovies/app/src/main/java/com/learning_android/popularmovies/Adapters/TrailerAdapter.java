package com.learning_android.popularmovies.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.learning_android.popularmovies.Objects.TrailerObject;
import com.learning_android.popularmovies.R;
import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>{
    private Context context;
    private ArrayList<TrailerObject> trailerList;
    private ItemClickListener itemClickListener;

    public TrailerAdapter(Context context, ArrayList<TrailerObject> trailerList, ItemClickListener listener) {
        this.context = context;
        this.trailerList = trailerList;
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.trailer_view, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        holder.tvWatchTrailer.setText(context.getText(R.string.watch_trailer) + " #"  + (position+1));
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    // ViewHolder class for this adapter
    class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvWatchTrailer;

        TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWatchTrailer = itemView.findViewById(R.id.tv_watch_trailer);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Log.d("Show", "onClick: Key : "+trailerList.get(adapterPosition).getVideoKey());
            itemClickListener.onItemClickListener(adapterPosition, trailerList.get(adapterPosition).getVideoKey());
        }
    }

    // Interface for implementing the onClickListener in ShowDetailsActivity class
    public interface ItemClickListener{
        void onItemClickListener(int clickedItemListener, String videoKey);
    }
}
