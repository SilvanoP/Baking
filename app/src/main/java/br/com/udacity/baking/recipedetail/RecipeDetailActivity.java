package br.com.udacity.baking.recipedetail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.com.udacity.baking.R;
import br.com.udacity.baking.recipeslist.Recipe;
import br.com.udacity.baking.utils.Constants;

public class RecipeDetailActivity extends AppCompatActivity {

    private Recipe mRecipe;

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
    }
}
