package br.com.udacity.baking.recipedetail;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

    private float quantity;
    private String measure;
    private String ingredient;

    public Ingredient() {
        quantity = 0f;
    }

    public Ingredient(Parcel parcel) {
        this.quantity = parcel.readFloat();
        this.measure = parcel.readString();
        this.ingredient = parcel.readString();
    }

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

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeFloat(quantity);
        parcel.writeString(measure);
        parcel.writeString(ingredient);
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel parcel) {
            return new Ingredient(parcel);
        }

        @Override
        public Ingredient[] newArray(int i) {
            return new Ingredient[i];
        }
    };
}
