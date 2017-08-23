package br.com.udacity.baking.recipeslist;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.udacity.baking.recipedetail.Ingredient;
import br.com.udacity.baking.recipedetail.RecipeStep;

public class Recipe implements Parcelable, Comparable<Recipe> {

    private Long id;
    private String name;
    private List<Ingredient> ingredients;
    private List<RecipeStep> steps;
    private int servings;
    private String image; // image url

    public Recipe() {
        ingredients = new ArrayList<>();
        steps = new ArrayList<>();
    }

    public Recipe(Parcel parcel) {
        ingredients = new ArrayList<>();
        steps = new ArrayList<>();

        this.id = parcel.readLong();
        this.name = parcel.readString();
        parcel.readTypedList(ingredients, Ingredient.CREATOR);
        parcel.readTypedList(steps, RecipeStep.CREATOR);
        this.servings = parcel.readInt();
        this.image = parcel.readString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<RecipeStep> getSteps() {
        return steps;
    }

    public void setSteps(List<RecipeStep> steps) {
        this.steps = steps;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public String getImage() {
        if (image == null) {
            image = "";
        }
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int compareTo(@NonNull Recipe recipe) {
        if (this.id > recipe.getId()) {
            return 1;
        } else if(this.id < recipe.getId()) {
            return -1;
        }
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeTypedList(ingredients);
        parcel.writeTypedList(steps);
        parcel.writeInt(servings);
        parcel.writeString(image);
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel parcel) {
            return new Recipe(parcel);
        }

        @Override
        public Recipe[] newArray(int i) {
            return new Recipe[i];
        }
    };
}
