/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.udacity.android.baking.data.source;

import android.support.annotation.NonNull;

import java.util.List;

import br.com.udacity.android.baking.data.Ingredient;
import br.com.udacity.android.baking.data.Recipe;
import br.com.udacity.android.baking.data.Step;

/**
 * Main entry point for accessing recipes data.
 */
public interface RecipesDataSource {

    interface LoadRecipesCallback {

        void onRecipesLoaded(List<Recipe> recipes);

        void onDataNotAvailable(int errorCode, String errorMessage);
    }

    interface LoadRecipeCallback {

        void onRecipeLoaded(Recipe recipe);

        void onDataNotAvailable();
    }

    boolean getRecipes(@NonNull LoadRecipesCallback callback);

    void getRecipe(int recipeId, @NonNull LoadRecipeCallback callback);

    void saveRecipe(@NonNull Recipe recipe);

    void refreshRecipes();

    void deleteAllRecipes();

    /*
     * RECIPE STEPS
     */

    interface LoadStepsCallback {

        void onStepsLoaded(List<Step> steps);

        void onDataNotAvailable();
    }

    interface LoadStepCallback {

        void onStepLoaded(Step step);

        void onDataNotAvailable();
    }

    void getSteps(int recipeId, @NonNull LoadStepsCallback callback);

    void getStep(int recipeId, int stepId, @NonNull LoadStepCallback callback);

    /*
     * RECIPE INGREDIENTS
     */

    interface LoadIngredientsCallback {

        void onIngredientsLoaded(List<Ingredient> ingredients);

        void onDataNotAvailable();
    }

    void getIngredients(int recipeId, @NonNull LoadIngredientsCallback callback);

    List<Ingredient> getIngredients(int recipeId);

}
