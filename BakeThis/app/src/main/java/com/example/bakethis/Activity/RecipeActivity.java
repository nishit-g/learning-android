package com.example.bakethis.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.bakethis.Adapters.StepsAdapter;
import com.example.bakethis.Adapters.ViewPagerAdapter;
import com.example.bakethis.Fragments.IngredientFragment;
import com.example.bakethis.Fragments.StepsFragment;
import com.example.bakethis.Helper.Constants;
import com.example.bakethis.Object.RecipeObject;
import com.example.bakethis.R;
import com.example.bakethis.databinding.ActivityRecipeBinding;
import com.google.android.material.tabs.TabLayout;

public class RecipeActivity extends AppCompatActivity {
    private ActivityRecipeBinding recipeLayout;
    private RecipeObject currentRecipe;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("recipe", "onCreate: Called");
        super.onCreate(savedInstanceState);

        recipeLayout = ActivityRecipeBinding.inflate(getLayoutInflater());
        setContentView(recipeLayout.getRoot());

        if(recipeLayout.isTablet==null){
            implementPhoneLayout();
        }
        else{
            implementTabletLayout();
        }

        Intent intent = getIntent();
        currentRecipe = intent.getParcelableExtra(Constants.RECIPE_OBJECT);

        setupActionBar();
    }

    private void implementTabletLayout() {

    }

    private void implementPhoneLayout() {
        viewPager = recipeLayout.vpRecipe;
        tabLayout = recipeLayout.tlRecipe;

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager.setAdapter(viewPagerAdapter);

    }

    private void setupActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(currentRecipe.getName());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

}
