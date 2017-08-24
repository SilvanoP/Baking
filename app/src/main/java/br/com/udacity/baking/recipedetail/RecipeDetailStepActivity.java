package br.com.udacity.baking.recipedetail;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

import br.com.udacity.baking.R;
import br.com.udacity.baking.utils.Constants;

public class RecipeDetailStepActivity extends AppCompatActivity implements RecipeDetailStepFragment.StepsCallback {

    private List<RecipeStep> mSteps;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_step);

        Intent intent = getIntent();
        if (intent.hasExtra(Constants.INTENT_EXTRA_LIST_STEP)) {

            int index = intent.getIntExtra(Constants.INTENT_EXTRA_STEP_INDEX, 0);
            mSteps = intent.getParcelableArrayListExtra(Constants.INTENT_EXTRA_LIST_STEP);

            mFragmentManager = getSupportFragmentManager();
            RecipeDetailStepFragment stepFragment = new RecipeDetailStepFragment();
            stepFragment.setAttributes(mSteps.get(index), index, this);
            mFragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_step_frame, stepFragment)
                    .commit();
        }
    }

    @Override
    public void goToVideo(int stepNumber) {
        if (stepNumber < 0) {
            Toast.makeText(this, R.string.warning_first_step, Toast.LENGTH_SHORT).show();
            return;
        } else if (stepNumber >= mSteps.size()) {
            Toast.makeText(this, R.string.warning_last_step, Toast.LENGTH_SHORT).show();
            return;
        }

        RecipeDetailStepFragment stepFragment = new RecipeDetailStepFragment();
        stepFragment.setAttributes(mSteps.get(stepNumber), stepNumber, this);
        mFragmentManager.beginTransaction()
                .replace(R.id.recipe_detail_step_frame, stepFragment)
                .commit();
    }
}
