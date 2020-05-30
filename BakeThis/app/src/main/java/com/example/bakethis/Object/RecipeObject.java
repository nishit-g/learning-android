package com.example.bakethis.Object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class RecipeObject implements Parcelable {
    private int id;
    private String name;
    private String imageUrl;
    private int servings;

    private ArrayList<IngredientObject> ingredientsList;
    private ArrayList<StepsObject> stepsList;

    public RecipeObject() {
    }

    protected RecipeObject(Parcel in) {
        id = in.readInt();
        name = in.readString();
        imageUrl = in.readString();
        servings = in.readInt();
        ingredientsList = in.createTypedArrayList(IngredientObject.CREATOR);
        stepsList = in.createTypedArrayList(StepsObject.CREATOR);
    }

    public static final Creator<RecipeObject> CREATOR = new Creator<RecipeObject>() {
        @Override
        public RecipeObject createFromParcel(Parcel in) {
            return new RecipeObject(in);
        }

        @Override
        public RecipeObject[] newArray(int size) {
            return new RecipeObject[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public ArrayList<IngredientObject> getIngredientsList() {
        return ingredientsList;
    }

    public void setIngredientsList(ArrayList<IngredientObject> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    public ArrayList<StepsObject> getStepsList() {
        return stepsList;
    }

    public void setStepsList(ArrayList<StepsObject> stepsList) {
        this.stepsList = stepsList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(imageUrl);
        dest.writeInt(servings);
        dest.writeTypedList(ingredientsList);
        dest.writeTypedList(stepsList);
    }
}
