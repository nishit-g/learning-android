package com.example.bakethis.Helper;

import android.content.Context;
import android.util.Log;

import com.example.bakethis.Object.IngredientObject;
import com.example.bakethis.Object.RecipeObject;
import com.example.bakethis.Object.StepsObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ParseJson {

    private ParseJson(){}


    public static ArrayList<RecipeObject> parseRecipes(Context context) {
        String json = null;
        ArrayList<RecipeObject> recipeList = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("baking.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");

            JSONArray jsonArray = new JSONArray(json);
            for(int i =0; i<jsonArray.length(); i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                RecipeObject recipeObject = new RecipeObject();
                ArrayList<IngredientObject> ingredientList = new ArrayList<>();
                ArrayList<StepsObject> stepsList = new ArrayList<>();

                recipeObject.setId(jsonObject.optInt("id"));
                recipeObject.setName(jsonObject.optString("name"));
                recipeObject.setServings(jsonObject.optInt("servings"));
                recipeObject.setImageUrl(jsonObject.optString("image"));

                JSONArray ingredientArray = jsonObject.getJSONArray("ingredients");
                JSONArray stepsArray = jsonObject.getJSONArray("steps");

                for(int j=0; j<ingredientArray.length(); j++){
                    IngredientObject ingredientObject = new IngredientObject();
                    JSONObject ingredientJsonObject = (JSONObject) ingredientArray.get(j);

                    ingredientObject.setQuantity(ingredientJsonObject.optInt("quantity"));
                    ingredientObject.setIngredients(ingredientJsonObject.optString("ingredient"));
                    ingredientObject.setMeasure(ingredientJsonObject.optString("measure"));

                    ingredientList.add(ingredientObject);
                }


                for(int k =0; k<stepsArray.length(); k++){
                    StepsObject stepsObject = new StepsObject();
                    JSONObject stepsJsonObject = (JSONObject) stepsArray.get(k);
                    String videoURL = stepsJsonObject.optString("videoURL");
                    Log.d("recipe", "parseRecipes: " + videoURL);
                    stepsObject.setDesc(stepsJsonObject.optString("description"));
                    stepsObject.setId(stepsJsonObject.optInt("id"));
                    stepsObject.setShortDesc(stepsJsonObject.optString("shortDescription"));
                    stepsObject.setThumbnailUrl(stepsJsonObject.optString("thumbnailURL"));
                    stepsObject.setVideoUrl(videoURL);

                    stepsList.add(stepsObject);
                }

                recipeObject.setIngredientsList(ingredientList);
                recipeObject.setStepsList(stepsList);

                recipeList.add(recipeObject);
            }

        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
            return null;
        }
        return recipeList;
    }
}
