package br.com.udacity.android.baking.ui.recipe;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.udacity.android.baking.R;
import br.com.udacity.android.baking.data.Ingredient;
import br.com.udacity.android.baking.data.source.RecipesDataSource;
import br.com.udacity.android.baking.data.source.RecipesRepository;
import br.com.udacity.android.baking.util.Inject;

public class IngredientsFragment extends Fragment implements IngredientAdapter.OnIngredientClickListener {

    public static final String ARG_RECIPE_ID = "recipe_id";
    private IngredientAdapter mAdapter;

    public IngredientsFragment() {
    }

    public static IngredientsFragment newInstance(int recipeId) {
        IngredientsFragment fragment = new IngredientsFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void loadIngredients(int recipeId) {
        RecipesRepository mRepository = Inject.getRecipeRepository(getContext());
        mRepository.getIngredients(recipeId, new RecipesDataSource.LoadIngredientsCallback() {
            @Override
            public void onIngredientsLoaded(List<Ingredient> ingredients) {
                mAdapter.setIngredients(ingredients);
            }

            @Override
            public void onDataNotAvailable() {
                Snackbar.make(getView(), getString(R.string.internal_error), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_ingredients, container, false);
        int recipeId = getArguments().getInt(ARG_RECIPE_ID);


        RecyclerView recyclerView = rootView.findViewById(R.id.recipe_ac_ingredient_list);
        mAdapter = new IngredientAdapter(this);
        recyclerView.setAdapter(mAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(divider);

        loadIngredients(recipeId);

        return rootView;
    }

    @Override
    public void onIngredientClick(Ingredient ingredient) {
        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, getString(R.string.ingredients_frag_query, ingredient.getIngredient()));
        startActivity(intent);
    }
}
