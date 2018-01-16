package br.com.udacity.android.baking.ui.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import br.com.udacity.android.baking.R;

public class IngredientsActivity extends AppCompatActivity {

    final private static String PARAM_RECIPE = "recipe";

    public static Intent getStartIntent(Context context, int recipeId) {
        Intent intent = new Intent(context, IngredientsActivity.class);
        intent.putExtra(PARAM_RECIPE, recipeId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            int recipeId = getIntent().getIntExtra(PARAM_RECIPE, -1);
            IngredientsFragment fragment = IngredientsFragment.newInstance(recipeId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.ingredients_ac_ingredients_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
