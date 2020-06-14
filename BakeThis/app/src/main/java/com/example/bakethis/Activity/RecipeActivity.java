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
import com.example.bakethis.Fragments.StepDetailFragment;
import com.example.bakethis.Fragments.StepsFragment;
import com.example.bakethis.Helper.Constants;
import com.example.bakethis.Object.RecipeObject;
import com.example.bakethis.R;
import com.example.bakethis.databinding.ActivityRecipeBinding;
import com.google.android.material.tabs.TabLayout;

public class RecipeActivity extends AppCompatActivity implements StepsFragment.OnStepFragmentClick {
    private ActivityRecipeBinding recipeLayout;
    private RecipeObject currentRecipe;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private boolean isTablet;
    private int stepSelected = 0;

    public int getStepSelected() {
        return stepSelected;
    }

    public void setStepSelected(int stepSelected) {
        this.stepSelected = stepSelected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recipeLayout = ActivityRecipeBinding.inflate(getLayoutInflater());
        setContentView(recipeLayout.getRoot());

        Intent intent = getIntent();
        currentRecipe = intent.getParcelableExtra(Constants.RECIPE_OBJECT);

        if(savedInstanceState!=null){
            isTablet = savedInstanceState.getBoolean(Constants.IS_FOR_TABLET_ACTIVITY);
            setStepSelected(savedInstanceState.getInt(Constants.SELECTED_STEP_INDEX));
        }

        if(recipeLayout.isTablet==null){
            isTablet = false;
            implementPhoneLayout();
        }
        else{
            isTablet = true;
            implementTabletLayout();
        }


        setupActionBar();
    }

    private void implementTabletLayout() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(fragmentManager.findFragmentByTag("stepDetailFrag")==null){
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setStepIndex(getStepSelected());
            stepDetailFragment.setForTablet(true);
            stepDetailFragment.setStepsList(currentRecipe.getStepsList());

            fragmentManager.beginTransaction()
                    .add(R.id.fl_step_detail,stepDetailFragment, "stepDetailFrag")
                    .commit();
        }
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
        outState.putBoolean(Constants.IS_FOR_TABLET_ACTIVITY,isTablet);
        outState.putInt(Constants.SELECTED_STEP_INDEX, getStepSelected());
    }

    @Override
    public void onStepFragClick(int stepIndex) {
        if(isTablet){
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            setStepSelected(stepIndex);
            stepDetailFragment.setStepIndex(stepIndex);
            stepDetailFragment.setForTablet(true);
            stepDetailFragment.setStepsList(currentRecipe.getStepsList());

            fragmentManager.beginTransaction()
                    .replace(R.id.fl_step_detail,stepDetailFragment,"stepDetailFrag")
                    .commit();
        }
        else{
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putParcelableArrayListExtra(Constants.STEPS_LIST, currentRecipe.getStepsList());
            intent.putExtra(Constants.STEP_INDEX, stepIndex);
            startActivity(intent);
        }
    }
}
