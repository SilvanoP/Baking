package br.com.udacity.android.baking.util;

import android.content.Context;

import br.com.udacity.android.baking.data.source.RecipesRepository;
import br.com.udacity.android.baking.data.source.local.BakingDatabase;
import br.com.udacity.android.baking.data.source.local.RecipesLocalDataSource;
import br.com.udacity.android.baking.data.source.remote.BakingServicesBuilder;
import br.com.udacity.android.baking.data.source.remote.RecipesRemoteDataSource;


public class Inject {
    public static RecipesRepository getRecipeRepository(Context context) {
        BakingDatabase database = BakingDatabase.getInstance(context);
        return RecipesRepository.getInstance(
                RecipesRemoteDataSource.getInstance(BakingServicesBuilder.build(context), context.getResources()),
                RecipesLocalDataSource.getInstance(new AppExecutors(), database.recipesDao(), database.stepsDao(), database.ingredientsDao()));
    }
}
