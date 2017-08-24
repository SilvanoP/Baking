package br.com.udacity.baking.recipedetail;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

import br.com.udacity.baking.R;
import br.com.udacity.baking.utils.Constants;

public class RecipeDetailStepActivity extends AppCompatActivity {

    private List<RecipeStep> mSteps;
    private RecipeStep mStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_step);

        Intent intent = getIntent();
        if (intent.hasExtra(Constants.INTENT_EXTRA_STEP)) {

            mStep = intent.getParcelableExtra(Constants.INTENT_EXTRA_STEP);
            mSteps = intent.getParcelableArrayListExtra(Constants.INTENT_EXTRA_LIST_STEP);

            FragmentManager fragmentManager = getSupportFragmentManager();
            RecipeDetailStepFragment stepFragment = new RecipeDetailStepFragment();
            stepFragment.setAttributes(mStep, mSteps.indexOf(mStep));
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_step_fragment, stepFragment)
                    .commit();
        }
    }
}
