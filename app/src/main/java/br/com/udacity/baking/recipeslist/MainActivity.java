package br.com.udacity.baking.recipeslist;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.udacity.baking.R;
import br.com.udacity.baking.recipedetail.RecipeDetailActivity;
import br.com.udacity.baking.service.RecipeService;
import br.com.udacity.baking.utils.Constants;
import br.com.udacity.baking.utils.ListItemClickListener;
import br.com.udacity.baking.utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public final String RECIPES_LIST_KEY = "RECIPES_LIST_KEY";

    @BindView(R.id.recipes_recycler_view)
    RecyclerView mRecipesRecyclerView;

    private ListItemClickListener mListener;
    private List<Recipe> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        RecyclerView.LayoutManager manager;
        if (getResources().getBoolean(R.bool.is_tablet)) {
            int numColumns = getResources().getInteger(R.integer.grid_layout_column_number);
            manager = new GridLayoutManager(this, numColumns);
        } else {
            manager = new LinearLayoutManager(this);
        }
        mRecipesRecyclerView.setLayoutManager(manager);

        mListener = new ListItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.i(MainActivity.class.getSimpleName(), "Item clicked!");
                Recipe recipe = mRecipes.get(position);
                Intent intent = new Intent(MainActivity.this, RecipeDetailActivity.class);
                intent.putExtra(Constants.INTENT_EXTRA_RECIPE, recipe);
                startActivity(intent);
            }
        };

        if (savedInstanceState != null) {
            mRecipes = savedInstanceState.getParcelableArrayList(RECIPES_LIST_KEY);
            if (mRecipes == null) {
                mRecipes = new ArrayList<>();
            }
            refreshRecipesAdapter();
        } else {
            getRecipesList();
        }
    }

    public void getRecipesList() {
        if (!Utils.isOnline(this)) {
            Toast.makeText(this, R.string.error_internet_connection, Toast.LENGTH_SHORT).show();
            return;
        }

        RecipeService service = new RecipeService();
        Call<List<Recipe>> response = service.getRecipes();

        if (response != null) {
            response.enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                    List<Recipe> recipes = response.body();
                    if (recipes != null) {
                        mRecipes = recipes;
                        refreshRecipesAdapter();
                    } else {
                        Log.e(MainActivity.class.getSimpleName(), "Error: " + response.code());
                        mRecipes = new ArrayList<>();
                    }
                }

                @Override
                public void onFailure(Call<List<Recipe>> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    public void refreshRecipesAdapter() {
        if (mRecipes.size() == 0) {
            Toast.makeText(this, R.string.error_no_recipe_found, Toast.LENGTH_SHORT).show();
            return;
        }

        RecipeListAdapter adapter = new RecipeListAdapter(this, mRecipes, mListener);
        mRecipesRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mRecipes != null) {
            outState.putParcelableArrayList(RECIPES_LIST_KEY, new ArrayList<Parcelable>(mRecipes));
        }
        super.onSaveInstanceState(outState);
    }
}
