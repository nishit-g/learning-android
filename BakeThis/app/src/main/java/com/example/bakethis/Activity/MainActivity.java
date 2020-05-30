package com.example.bakethis.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.example.bakethis.Adapters.HomepageAdapter;
import com.example.bakethis.Helper.Constants;
import com.example.bakethis.Helper.ParseJson;
import com.example.bakethis.Object.RecipeObject;
import com.example.bakethis.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements HomepageAdapter.TouchListener {

    private ActivityMainBinding layoutBinding;
    private RecyclerView rvHomePage;
    private ArrayList<RecipeObject> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layoutBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = layoutBinding.getRoot();
        setContentView(view);

        rvHomePage = layoutBinding.rvHomepage;

        inflateRecyclerView();
    }

    private void inflateRecyclerView() {
        recipeList = ParseJson.parseRecipes(this);

        int spanCount = calculateNoOfColumns();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount, LinearLayoutManager.VERTICAL, false);
        rvHomePage.setLayoutManager(gridLayoutManager);

        HomepageAdapter homepageAdapter = new HomepageAdapter(this, recipeList,this);
        rvHomePage.setAdapter(homepageAdapter);
    }

    public int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 180;
        int noOfColumns = (int) (dpWidth/scalingFactor);
        if(noOfColumns<2)
            noOfColumns = 2;
        return noOfColumns;
    }

    @Override
    public void onRecipeSelect(int position) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(Constants.RECIPE_OBJECT,recipeList.get(position));
        startActivity(intent);
    }
}
