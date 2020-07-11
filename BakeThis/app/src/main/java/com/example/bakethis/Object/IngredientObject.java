package com.example.bakethis.Object;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class IngredientObject implements Parcelable {

    @SerializedName("quantity")
    private float quantity;
    @SerializedName("measure")
    private String measure;
    @SerializedName("ingredient")
    private String ingredients;


    public IngredientObject() {
    }

    protected IngredientObject(Parcel in) {
        quantity = in.readFloat();
        measure = in.readString();
        ingredients = in.readString();
    }

    public static final Creator<IngredientObject> CREATOR = new Creator<IngredientObject>() {
        @Override
        public IngredientObject createFromParcel(Parcel in) {
            return new IngredientObject(in);
        }

        @Override
        public IngredientObject[] newArray(int size) {
            return new IngredientObject[size];
        }
    };

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(quantity);
        dest.writeString(measure);
        dest.writeString(ingredients);
    }
}
