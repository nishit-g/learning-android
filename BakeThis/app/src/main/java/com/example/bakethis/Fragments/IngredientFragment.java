package com.example.bakethis.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bakethis.Adapters.IngredientAdapter;
import com.example.bakethis.Helper.Constants;
import com.example.bakethis.Object.IngredientObject;
import com.example.bakethis.Object.RecipeObject;
import com.example.bakethis.databinding.FragmentIngredientLayoutBinding;

import java.util.ArrayList;

public class IngredientFragment extends Fragment {
    public IngredientFragment(){ }
    private RecipeObject recipeObject;
    private ArrayList<IngredientObject> ingredientList;
    private FragmentIngredientLayoutBinding ingredientLayout;
    private RecyclerView rvIngredient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ingredientLayout = FragmentIngredientLayoutBinding.inflate(inflater,container,false);
        rvIngredient = ingredientLayout.rvIngredients;
        Intent intent = getActivity().getIntent();

        if(savedInstanceState!=null){
            recipeObject = savedInstanceState.getParcelable(Constants.RECIPE_OBJECT);
        }
        else if(intent!=null){
            recipeObject = intent.getParcelableExtra(Constants.RECIPE_OBJECT);
        }
        inflateRecyclerView();
        setRetainInstance(true);
        return ingredientLayout.getRoot();
    }

    private void inflateRecyclerView() {

        ingredientList = recipeObject.getIngredientsList();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rvIngredient.setLayoutManager(layoutManager);
        rvIngredient.setHasFixedSize(true);
        IngredientAdapter ingredientAdapter = new IngredientAdapter(ingredientList,getContext());
        rvIngredient.setAdapter(ingredientAdapter);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.RECIPE_OBJECT, recipeObject);
    }

    @Override
    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(retain);
    }
}
