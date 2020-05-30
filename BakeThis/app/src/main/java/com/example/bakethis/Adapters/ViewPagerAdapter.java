package com.example.bakethis.Adapters;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.bakethis.Fragments.IngredientFragment;
import com.example.bakethis.Fragments.StepsFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fragmentManager){
        super((fragmentManager));
    }



    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: IngredientFragment ingredientFragment= new IngredientFragment();
                    ingredientFragment.setRetainInstance(true);
                    return ingredientFragment;
            case 1: return new StepsFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Ingredient(s)";
            case 1: return "Step(s)";
            default: return null;
        }
    }

}
