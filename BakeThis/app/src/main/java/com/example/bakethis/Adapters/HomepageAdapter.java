package com.example.bakethis.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakethis.Object.RecipeObject;
import com.example.bakethis.R;
import com.example.bakethis.databinding.HomepageSingleBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomepageAdapter extends RecyclerView.Adapter<HomepageAdapter.HomepageView> {
    private HomepageSingleBinding rvLayout;
    private Context context;
    private ArrayList<RecipeObject> recipeList;
    private TouchListener touchListener;

    public HomepageAdapter(Context context, ArrayList<RecipeObject> recipeList, TouchListener touchListener){
        this.recipeList = recipeList;
        this.context = context;
        this.touchListener = touchListener;
    }

    public interface TouchListener{
        void onRecipeSelect(int position);
    }
    @NonNull
    @Override
    public HomepageView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomepageSingleBinding view = HomepageSingleBinding.inflate(LayoutInflater.from(context));
        return new HomepageView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomepageView holder, int position) {
        holder.tvServings.setText("Servings : " + recipeList.get(position).getServings());
        holder.tvName.setText(recipeList.get(position).getName());

        int imageID = R.drawable.food4;
        switch (position){
            case 0: imageID =  R.drawable.food1; break;
            case 1 : imageID = R.drawable.food2; break;
            case 2: imageID = R.drawable.food3; break;
            case 3: imageID = R.drawable.food4;break;
        }

        if(!recipeList.get(position).getImageUrl().isEmpty()){
            Picasso.get()
                    .load(recipeList.get(position).getImageUrl())
                    .placeholder(imageID)
                    .error(R.drawable.error_placeholder)
                    .into(holder.ivRecipeImage);
        }
        else{
            Picasso.get()
                    .load(imageID)
                    .into(holder.ivRecipeImage);
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }


     class HomepageView extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvName;
        TextView tvServings;
        ImageView ivRecipeImage;
        HomepageView(@NonNull HomepageSingleBinding itemView) {
            super(itemView.getRoot());
            rvLayout = itemView;
            tvName = rvLayout.tvRname;
            tvServings = rvLayout.tvServings;
            ivRecipeImage = rvLayout.ivRecipeImage;
            itemView.getRoot().setOnClickListener(this);
        }

         @Override
         public void onClick(View v) {
            touchListener.onRecipeSelect(getAdapterPosition());
         }
     }
}
