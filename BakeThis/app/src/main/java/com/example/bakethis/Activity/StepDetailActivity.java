package com.example.bakethis.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.bakethis.Fragments.StepDetailFragment;
import com.example.bakethis.Helper.Constants;
import com.example.bakethis.Object.StepsObject;
import com.example.bakethis.R;
import com.example.bakethis.databinding.ActivityStepDetailBinding;

import java.util.ArrayList;

public class StepDetailActivity extends AppCompatActivity {
    private ActivityStepDetailBinding layout;
    private FrameLayout frameLayout;
    private ArrayList<StepsObject> stepsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = ActivityStepDetailBinding.inflate(getLayoutInflater());
        setContentView(layout.getRoot());

        frameLayout = layout.fContainer;

        setupActivity();

        Intent intent = getIntent();
        if(intent!=null){
            stepsList = intent.getParcelableArrayListExtra(Constants.STEPS_LIST);
        }

        if(savedInstanceState==null){
            StepDetailFragment stepDetailFragment = new StepDetailFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.f_container,stepDetailFragment)
                    .commit();
        }
    }

    private void setupActivity() {
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
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
}
