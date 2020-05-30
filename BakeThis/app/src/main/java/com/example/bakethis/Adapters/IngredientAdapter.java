package com.example.bakethis.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakethis.Object.IngredientObject;
import com.example.bakethis.databinding.IngredientSingleBinding;

import java.util.ArrayList;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.SingleIngredient> {
    private IngredientSingleBinding rvLayout;
    private ArrayList<IngredientObject> ingredientList;
    private Context context;

    public IngredientAdapter(ArrayList<IngredientObject> ingredientList, Context context) {
        this.ingredientList = ingredientList;
        this.context = context;
    }

    @NonNull
    @Override
    public SingleIngredient onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        IngredientSingleBinding view = IngredientSingleBinding.inflate(LayoutInflater.from(context));
        return new SingleIngredient(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleIngredient holder, int position) {
            IngredientObject currentIngredient = ingredientList.get(position);
            holder.tvIngredients.setText(currentIngredient.getIngredients());
            holder.tvMeasureAndQuant.setText(currentIngredient.getQuantity() + " " + currentIngredient.getMeasure());
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }


    class SingleIngredient extends RecyclerView.ViewHolder {
        TextView tvIngredients;
        TextView tvMeasureAndQuant;
        SingleIngredient(@NonNull IngredientSingleBinding itemView) {
            super(itemView.getRoot());
            rvLayout = itemView;
            tvIngredients = itemView.tvIngredients;
            tvMeasureAndQuant = itemView.tvQuantityAndMeasure;
        }
    }
}
