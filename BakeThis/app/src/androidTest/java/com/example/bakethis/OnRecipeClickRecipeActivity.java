package com.example.bakethis;


import android.content.Intent;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import com.example.bakethis.Activity.MainActivity;
import com.example.bakethis.Activity.RecipeActivity;
import com.example.bakethis.Activity.StepDetailActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(JUnit4.class)
public class OnRecipeClickRecipeActivity {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void initIntent(){
        Intents.init();
    }

    @Test
    public void clickOnRecipe_BringsRecipeActivity(){

        onView(withId(R.id.rv_homepage))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));

        intended(hasComponent(RecipeActivity.class.getName()));

    }
}
