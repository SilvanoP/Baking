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

package br.com.udacity.android.baking.data.source.remote;

import android.content.res.Resources;
import android.support.annotation.NonNull;

import java.util.List;

import br.com.udacity.android.baking.R;
import br.com.udacity.android.baking.data.Ingredient;
import br.com.udacity.android.baking.data.Recipe;
import br.com.udacity.android.baking.data.source.RecipesDataSource;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Implementation of the data source that adds a latency simulating network.
 */
public class RecipesRemoteDataSource implements RecipesDataSource {

    private static RecipesRemoteDataSource INSTANCE;

    private static final int ERROR_CODE_DEFAULT = 1000;
    private static final int SERVICE_LATENCY_IN_MILLIS = 2000; // used to simulate network delay

    private final Resources mResources;
    private final BakingServices mBakingServices;

    public static RecipesRemoteDataSource getInstance(BakingServices bakingServices, Resources resources) {
        if (INSTANCE == null) {
            INSTANCE = new RecipesRemoteDataSource(bakingServices, resources);
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private RecipesRemoteDataSource(BakingServices bakingServices, Resources resources) {
        mBakingServices = bakingServices;
        mResources = resources;
    }


    @Override
    public boolean getRecipes(@NonNull final LoadRecipesCallback callback) {
        Call call = mBakingServices.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    List<Recipe> recipes = (List<Recipe>) response.body();
                    if (recipes == null) {
                        callback.onDataNotAvailable(ERROR_CODE_DEFAULT,
                                mResources.getString(R.string.remote_services_request_error_get_recipes));
                        return;
                    }
                    callback.onRecipesLoaded(recipes);
                } else {
                    callback.onDataNotAvailable(response.code(),
                            mResources.getString(R.string.remote_services_request_error_get_recipes));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                callback.onDataNotAvailable(ERROR_CODE_DEFAULT,
                        mResources.getString(R.string.connection_error));
            }

        });
        return false;
    }

    @Override
    public void getRecipe(int recipeId, @NonNull LoadRecipeCallback callback) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveRecipe(@NonNull Recipe recipe) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void refreshRecipes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAllRecipes() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getSteps(int recipeId, @NonNull LoadStepsCallback callback) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getStep(int recipeId, int stepId, @NonNull LoadStepCallback callback) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void getIngredients(int recipeId, @NonNull LoadIngredientsCallback callback) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Ingredient> getIngredients(int recipeId) {
        throw new UnsupportedOperationException();
    }
}
