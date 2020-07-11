package com.example.bakethis.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.bakethis.Adapters.HomepageAdapter;
import com.example.bakethis.Helper.BakingAPI;
import com.example.bakethis.Helper.BakingNetwork;
import com.example.bakethis.Helper.Constants;
import com.example.bakethis.Helper.ParseJson;
import com.example.bakethis.Widget.IngredientWidgetProvider;
import com.example.bakethis.Object.RecipeObject;
import com.example.bakethis.databinding.ActivityMainBinding;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements HomepageAdapter.TouchListener {

    private ActivityMainBinding layoutBinding;
    private RecyclerView rvHomePage;
    private ArrayList<RecipeObject> recipeList;
    private boolean isTablet = false;

    private BakingAPI bakingAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layoutBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = layoutBinding.getRoot();
        setContentView(view);

        rvHomePage = layoutBinding.rvHomepage;

        bakingAPI = BakingNetwork.getInstance(this);

        if(layoutBinding.isTablet!=null){
            isTablet = true;
        }

        inflateRecyclerView();
    }

    private void inflateRecyclerView() {
//        recipeList = ParseJson.parseRecipes(this);
        Call<ArrayList<RecipeObject>> call = bakingAPI.getRecipes();
        call.enqueue(new Callback<ArrayList<RecipeObject>>() {
            @Override
            public void onResponse(Call<ArrayList<RecipeObject>> call, Response<ArrayList<RecipeObject>> response) {
                if(response.isSuccessful()){

                    recipeList = response.body();
                    Log.d("recipe", "onResponse: "  +recipeList.size());
                    Log.d("recipe", "onResponse: "  +recipeList.get(0).getIngredientsList().size());

                    int spanCount = calculateNoOfColumns();
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, spanCount, LinearLayoutManager.VERTICAL, false);
                    rvHomePage.setLayoutManager(gridLayoutManager);

                    HomepageAdapter homepageAdapter = new HomepageAdapter(MainActivity.this, recipeList, MainActivity.this);
                    rvHomePage.setAdapter(homepageAdapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<RecipeObject>> call, Throwable t) {
                    t.printStackTrace();
            }
        });

    }

    public int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = (isTablet)?280:180;
        int noOfColumns = (int) (dpWidth/scalingFactor);
        if(noOfColumns<2)
            noOfColumns = 2;
        return noOfColumns;
    }

    @Override
    public void onRecipeSelect(int position) {
        //update the preferences and send the broadcast to the widgets to change the values
        updateSharedPreference(recipeList.get(position));
        sendBroadcastToWidget();

        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra(Constants.RECIPE_OBJECT,recipeList.get(position));
        startActivity(intent);
    }

    private void sendBroadcastToWidget() {
        Log.d("widget", "Sending broadcast to the widget ...... ");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientWidgetProvider.class));

        Intent updateAppWidgetIntent = new Intent();
        updateAppWidgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        updateAppWidgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        sendBroadcast(updateAppWidgetIntent);
    }

    private void updateSharedPreference(RecipeObject recipeObject) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Need only name of the recipe and ingredients of the it.

        editor.putString(Constants.PREF_RECIPE_NAME, recipeObject.getName());

        Gson gson = new Gson();
        String ingredientList = gson.toJson(recipeObject.getIngredientsList());

        editor.putString(Constants.PREF_RECIPE_LIST, ingredientList);

        editor.apply();
    }
}
