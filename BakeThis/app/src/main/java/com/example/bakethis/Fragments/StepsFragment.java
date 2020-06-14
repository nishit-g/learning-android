package com.example.bakethis.Fragments;

import android.content.Context;
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
import com.example.bakethis.Activity.StepDetailActivity;
import com.example.bakethis.Adapters.StepsAdapter;
import com.example.bakethis.Helper.Constants;
import com.example.bakethis.Object.RecipeObject;
import com.example.bakethis.Object.StepsObject;
import com.example.bakethis.databinding.FragmentStepsBinding;
import java.util.ArrayList;

public class StepsFragment extends Fragment implements StepsAdapter.StepsClickListener {
    private FragmentStepsBinding stepsLayout;
    private RecyclerView rvSteps;
    private ArrayList<StepsObject> stepsList;
    private RecipeObject currentRecipe;

    private OnStepFragmentClick mCallback;

    public interface OnStepFragmentClick{
        void onStepFragClick(int stepIndex);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        stepsLayout = FragmentStepsBinding.inflate(inflater,container,false);

        rvSteps = stepsLayout.fSteps;

        Intent intent = getActivity().getIntent();

        if(intent !=null){
            currentRecipe = intent.getParcelableExtra(Constants.RECIPE_OBJECT);
            stepsList = currentRecipe.getStepsList();

            inflateFragment();
        }

        return stepsLayout.getRoot();
    }

    private void inflateFragment() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvSteps.setLayoutManager(layoutManager);
        StepsAdapter stepsAdapter = new StepsAdapter(getContext(),stepsList, this);
        rvSteps.setAdapter(stepsAdapter);
    }

    @Override
    public void onStepClick(int position) {
        mCallback.onStepFragClick(position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnStepFragmentClick) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnStepFragmentClick");
        }
    }
}
