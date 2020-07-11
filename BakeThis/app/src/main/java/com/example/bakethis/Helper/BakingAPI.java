package com.example.bakethis.Helper;

import com.example.bakethis.Object.RecipeObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingAPI {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<ArrayList<RecipeObject>>  getRecipes();

}
