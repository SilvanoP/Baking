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

package br.com.udacity.android.baking.data.source.local;

import android.support.annotation.NonNull;

import java.util.List;

import br.com.udacity.android.baking.data.Ingredient;
import br.com.udacity.android.baking.data.Recipe;
import br.com.udacity.android.baking.data.Step;
import br.com.udacity.android.baking.data.source.RecipesDataSource;
import br.com.udacity.android.baking.util.AppExecutors;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Concrete implementation of a data source as a db.
 */
public class RecipesLocalDataSource implements RecipesDataSource {

    private static volatile RecipesLocalDataSource INSTANCE;

    private RecipesDao mRecipesDao;
    private StepsDao mStepsDao;
    private IngredientsDao mIngredientsDao;

    private AppExecutors mAppExecutors;

    // Prevent direct instantiation.
    private RecipesLocalDataSource(@NonNull AppExecutors appExecutors,
                                   @NonNull RecipesDao recipesDao,
                                   @NonNull StepsDao stepsDao,
                                   @NonNull IngredientsDao ingredientsDao) {
        mAppExecutors = appExecutors;
        mRecipesDao = recipesDao;
        mStepsDao = stepsDao;
        mIngredientsDao = ingredientsDao;
    }

    public static RecipesLocalDataSource getInstance(@NonNull AppExecutors appExecutors,
                                                     @NonNull RecipesDao recipesDao,
                                                     @NonNull StepsDao stepsDao,
                                                     @NonNull IngredientsDao ingredientsDao) {
        if (INSTANCE == null) {
            synchronized (RecipesLocalDataSource.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RecipesLocalDataSource(appExecutors,
                            recipesDao, stepsDao, ingredientsDao);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Note: {@link LoadRecipesCallback#onDataNotAvailable(int errorCode, String errorMessage)} is fired if the database doesn't exist
     * or the table is empty.
     */
    @Override
    public boolean getRecipes(@NonNull final LoadRecipesCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Recipe> recipes = mRecipesDao.getRecipes();
                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (recipes.isEmpty()) {
                            // This will be called if the table is new or just empty.
                            callback.onDataNotAvailable(-1, null);
                        } else {
                            callback.onRecipesLoaded(recipes);
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
        return false;
    }

    /**
     * Note: {@link LoadRecipeCallback#onDataNotAvailable()} is fired if the {@link Recipe} isn't
     * found.
     */
    @Override
    public void getRecipe(final int recipeId, @NonNull final LoadRecipeCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Recipe recipe = mRecipesDao.getRecipeById(recipeId);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (recipe != null) {
                            callback.onRecipeLoaded(recipe);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void saveRecipe(@NonNull final Recipe recipe) {
        checkNotNull(recipe);
        Runnable saveRunnable = new Runnable() {
            @Override
            public void run() {
                mRecipesDao.insertRecipe(recipe);
                // recipe id is empty if it comes from remote.
                for (Step step : recipe.getSteps()) {
                    step.setRecipeId(recipe.getId());
                }
                for (int i = 0; i < recipe.getIngredients().size(); i++) {
                    Ingredient ingredient = recipe.getIngredients().get(i);
                    ingredient.setId(i + 1);
                    ingredient.setRecipeId(recipe.getId());
                }
                mStepsDao.insertAll(recipe.getSteps());
                mIngredientsDao.insertAll(recipe.getIngredients());
            }
        };
        mAppExecutors.diskIO().execute(saveRunnable);
    }

    @Override
    public void refreshRecipes() {
        // Not required because the {@link RecipesRepository} handles the logic of refreshing the
        // recipes from all the available data sources.
    }

    @Override
    public void deleteAllRecipes() {
        Runnable deleteRunnable = new Runnable() {
            @Override
            public void run() {
                mRecipesDao.deleteRecipes();
            }
        };

        mAppExecutors.diskIO().execute(deleteRunnable);
    }

    @Override
    public void getSteps(final int recipeId, @NonNull final LoadStepsCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Step> steps = mStepsDao.getSteps(recipeId);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (steps != null) {
                            callback.onStepsLoaded(steps);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getStep(final int recipeId, final int stepId, @NonNull final LoadStepCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Step step = mStepsDao.getStep(recipeId, stepId);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (step != null) {
                            callback.onStepLoaded(step);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public void getIngredients(final int recipeId, @NonNull final LoadIngredientsCallback callback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Ingredient> ingredients = mIngredientsDao.getIngredients(recipeId);

                mAppExecutors.mainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (ingredients != null) {
                            callback.onIngredientsLoaded(ingredients);
                        } else {
                            callback.onDataNotAvailable();
                        }
                    }
                });
            }
        };

        mAppExecutors.diskIO().execute(runnable);
    }

    @Override
    public List<Ingredient> getIngredients(int recipeId) {
        return mIngredientsDao.getIngredients(recipeId);
    }
}
