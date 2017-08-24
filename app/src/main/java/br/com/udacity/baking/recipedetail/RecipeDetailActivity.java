package br.com.udacity.baking.recipedetail;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import br.com.udacity.baking.R;
import br.com.udacity.baking.recipeslist.Recipe;
import br.com.udacity.baking.utils.Constants;

public class RecipeDetailActivity extends AppCompatActivity {

    private Recipe mRecipe;
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Intent intent = getIntent();
        if (!intent.hasExtra(Constants.INTENT_EXTRA_RECIPE)) {
            finish();
        }

        mRecipe = intent.getParcelableExtra(Constants.INTENT_EXTRA_RECIPE);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(mRecipe.getName());
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        recipeDetailFragment.setRecipe(mRecipe);
        fragmentManager.beginTransaction()
                .add(R.id.recipe_detail_fragment, recipeDetailFragment)
                .commit();

        if (getResources().getBoolean(R.bool.is_tablet)) {
            // If it is a tablet, then it has two panes
            mTwoPane = true;

            RecipeDetailStepFragment stepFragment = new RecipeDetailStepFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_step_fragment, stepFragment)
                    .commit();
        } else {
            mTwoPane = false;
        }
    }
}
