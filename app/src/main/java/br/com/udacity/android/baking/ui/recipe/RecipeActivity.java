package br.com.udacity.android.baking.ui.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import br.com.udacity.android.baking.R;
import br.com.udacity.android.baking.data.Recipe;
import br.com.udacity.android.baking.data.Step;
import br.com.udacity.android.baking.data.source.RecipesDataSource;
import br.com.udacity.android.baking.data.source.RecipesRepository;
import br.com.udacity.android.baking.util.Inject;

import static android.support.v4.app.NavUtils.navigateUpFromSameTask;

/**
 * An activity representing a list of RecipeItems. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link StepActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeActivity extends AppCompatActivity implements StepAdapter.OnStepClickListener {


    final private static String LOG_TAG = "RecipeActivity";

    final private static String PARAM_RECIPE = "recipe";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    // TODO: It should be injected.
    private RecipesRepository mRecipesRepository;
    private StepAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private int mRecipeId;


    public static Intent getStartIntent(Context context, Recipe recipe) {
        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra(PARAM_RECIPE, recipe.getId());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        mRecipeId = getIntent().getIntExtra(PARAM_RECIPE, -1);

        mRecipesRepository = Inject.getRecipeRepository(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (findViewById(R.id.recipe_ac_recipeitem_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        mRecyclerView = findViewById(R.id.recipe_ac_step_list);
        mAdapter = new StepAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        findViewById(R.id.recipe_ac_ingredient_cardview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTwoPane) {
                    IngredientsFragment fragment = IngredientsFragment.newInstance(mRecipeId);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_ac_recipeitem_detail_container, fragment)
                            .commit();
                } else {
                    Intent intent = IngredientsActivity.getStartIntent(RecipeActivity.this, mRecipeId);
                    startActivity(intent);
                }
            }
        });

        loadRecipe();
    }

    private void loadRecipe() {
        mRecipesRepository.getSteps(mRecipeId, new RecipesDataSource.LoadStepsCallback() {
            @Override
            public void onStepsLoaded(List<Step> steps) {
                mAdapter.setSteps(steps);
            }

            @Override
            public void onDataNotAvailable() {
                Snackbar.make(mRecyclerView, getString(R.string.internal_error), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStepClick(Step step) {
        if (mTwoPane) {
            StepFragment fragment = StepFragment.newInstance(step.getRecipeId(), step.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_ac_recipeitem_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = StepActivity.getStartIntent(this, step);
            startActivity(intent);
        }
    }
}
