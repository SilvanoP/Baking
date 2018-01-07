package br.com.udacity.baking.recipedetail;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.udacity.baking.R;
import br.com.udacity.baking.recipeslist.Recipe;
import br.com.udacity.baking.utils.Constants;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailStepFragment.StepsCallback {

    private final String RECIPE_KEY = "RECIPE_KEY";
    private final String STEP_INDEX_KEY = "STEP_INDEX_KEY";

    private Recipe mRecipe;
    private int mStepIndex;

    FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Intent intent = getIntent();
        if (intent.hasExtra(Constants.INTENT_EXTRA_RECIPE)) {
            mRecipe = intent.getParcelableExtra(Constants.INTENT_EXTRA_RECIPE);
            mStepIndex = 0;
        }

        if (mRecipe == null && savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(RECIPE_KEY);
            mStepIndex = savedInstanceState.getInt(STEP_INDEX_KEY);
        }


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(mRecipe.getName());
        }

        mFragmentManager = getSupportFragmentManager();
        if (mFragmentManager.findFragmentById(R.id.recipe_detail_fragment) == null) {
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setRecipe(mRecipe);
            mFragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_fragment, recipeDetailFragment)
                    .commit();
        }

        if (getResources().getBoolean(R.bool.is_tablet)) {
            // If it is a tablet, then it has two panes

            RecipeDetailStepFragment stepFragment = null;
            Fragment f = mFragmentManager.findFragmentById(R.id.recipe_detail_step_fragment);
            if (f != null && f instanceof RecipeDetailStepFragment) {
                stepFragment = (RecipeDetailStepFragment) f;
                stepFragment.setCallback(this);
            } else {
                stepFragment = new RecipeDetailStepFragment();
                stepFragment.setAttributes(mRecipe.getSteps().get(mStepIndex), mStepIndex, this);
                mFragmentManager.beginTransaction()
                        .add(R.id.recipe_detail_step_fragment, stepFragment)
                        .commit();
            }
        }

    }

    /**
     * This is only used here when the device is tablet.
     * @param stepNumber The current shown step that will be updated
     */
    @Override
    public void goToVideo(int stepNumber) {
        List<RecipeStep> steps = mRecipe.getSteps();
        if (stepNumber < 0) {
            Toast.makeText(this, R.string.warning_first_step, Toast.LENGTH_SHORT).show();
        } else if (stepNumber >= steps.size()) {
            Toast.makeText(this, R.string.warning_last_step, Toast.LENGTH_SHORT).show();
        } else {
            RecipeDetailStepFragment stepFragment = new RecipeDetailStepFragment();
            stepFragment.setAttributes(steps.get(stepNumber), stepNumber, this);
            mFragmentManager.beginTransaction()
                    .replace(R.id.recipe_detail_step_fragment, stepFragment)
                    .commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putInt(STEP_INDEX_KEY, mStepIndex);
        outState.putParcelable(RECIPE_KEY, mRecipe);
    }
}
