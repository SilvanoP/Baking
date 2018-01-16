package br.com.udacity.android.baking.ui.recipes;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import br.com.udacity.android.baking.R;
import br.com.udacity.android.baking.data.Recipe;
import br.com.udacity.android.baking.data.source.RecipesDataSource;
import br.com.udacity.android.baking.data.source.RecipesRepository;
import br.com.udacity.android.baking.ui.BaseActivity;
import br.com.udacity.android.baking.ui.recipe.RecipeActivity;
import br.com.udacity.android.baking.util.Inject;

public class RecipesActivity extends BaseActivity implements View.OnClickListener, RecipesAdapter.OnRecipeClickListener {

    // TODO: It should be injected.
    private RecipesRepository mRecipesRepository;
    private RecipesAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        initBaseActivity();
        setOnErrorClickListener(this);

        mRecipesRepository = Inject.getRecipeRepository(this);

        // Add swipe to refresh event
        mSwipeRefreshLayout = findViewById(R.id.recipes_ac_swiperefreshlayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadRecipes(true);
            }
        });
        // Init RecycleView, Adapter and LayoutManager
        RecyclerView mRecyclerView = findViewById(R.id.recipes_ac_recycleview);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.recipes_ac_grid_spancount));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecipesAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        loadRecipes(false);
    }

    public void loadRecipes(boolean forced) {
        if(forced) {
            mRecipesRepository.refreshRecipes();
        }
        boolean cached = mRecipesRepository.getRecipes(new RecipesDataSource.LoadRecipesCallback() {
            @Override
            public void onRecipesLoaded(List<Recipe> recipes) {
                showContent();
                mAdapter.setRecipes(recipes);
            }

            @Override
            public void onDataNotAvailable(int errorCode, String errorMessage) {
                showError(String.format("(%d) %s", errorCode, errorMessage));
            }
        });
        if(!cached) {
            showLoading("Loading...");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_error_button:
                loadRecipes(true);
                break;
            default:
                throw new UnsupportedOperationException("There is no click for view " + view.getId());
        }
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Snackbar.make(mSwipeRefreshLayout, recipe.getName(), Snackbar.LENGTH_SHORT).show();
        startActivity(RecipeActivity.getStartIntent(this, recipe));
    }

    @Override
    public void showContent() {
        mSwipeRefreshLayout.setRefreshing(false);
        super.showContent();
    }
}
