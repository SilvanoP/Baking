package br.com.udacity.baking.recipedetail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.udacity.baking.R;
import br.com.udacity.baking.recipeslist.Recipe;
import br.com.udacity.baking.utils.Constants;
import br.com.udacity.baking.utils.ListItemClickListener;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment {

    private final String RECIPE_KEY = "RECIPE_KEY";

    @BindView(R.id.recipe_ingredient_recycler)
    RecyclerView mIngredientRecyclerView;
    @BindView(R.id.recipe_steps_recycler)
    RecyclerView mStepsRecyclerView;

    private Recipe mRecipe;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailFragment newInstance(String param1, String param2) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(RECIPE_KEY);
        }

        if (mRecipe == null) {
            Toast.makeText(getContext(), R.string.error_no_recipe_found, Toast.LENGTH_LONG).show();
        } else {
            IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(getContext(), mRecipe.getIngredients());
            mIngredientRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mIngredientRecyclerView.setAdapter(ingredientsAdapter);

            ListItemClickListener itemClickListener = new ListItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(getContext(), RecipeDetailStepActivity.class);
                    intent.putExtra(Constants.INTENT_EXTRA_STEP_INDEX, position);
                    intent.putExtra(Constants.INTENT_EXTRA_LIST_STEP, new ArrayList<Parcelable>(mRecipe.getSteps()));
                    startActivity(intent);
                }
            };

            RecipeStepAdapter stepAdapter = new RecipeStepAdapter(getContext(), itemClickListener, mRecipe.getSteps());
            mStepsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            mStepsRecyclerView.setAdapter(stepAdapter);

        }
        return view;
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(RECIPE_KEY, mRecipe);
    }
}
