package br.com.udacity.android.baking.widgets.recipe;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import br.com.udacity.android.baking.R;
import br.com.udacity.android.baking.data.Recipe;
import br.com.udacity.android.baking.data.source.RecipesDataSource;
import br.com.udacity.android.baking.data.source.RecipesRepository;
import br.com.udacity.android.baking.ui.BaseActivity;
import br.com.udacity.android.baking.util.Inject;

/**
 * The configuration screen for the {@link RecipeWidget RecipeWidget} AppWidget.
 */
public class RecipeWidgetConfigureActivity extends BaseActivity implements RecipeWidgetConfigureAdapter.OnRecipeClickListener, View.OnClickListener {

    private static final String PREFS_NAME = "br.com.udacity.android.baking.widgets.recipe.RecipeWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";


    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private RecipesRepository mRecipesRepository;
    private RecipeWidgetConfigureAdapter mAdapter;

    public RecipeWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveRecipePref(Context context, int appWidgetId, int recipeId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + appWidgetId, recipeId);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static int loadRecipePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        int recipeId = prefs.getInt(PREF_PREFIX_KEY + appWidgetId, 0);
        return recipeId;
    }

    static void deleteRecipePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.recipe_widget_configure);
        initBaseActivity();
        setOnErrorClickListener(this);

        mRecipesRepository = Inject.getRecipeRepository(this);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        // Init RecycleView, Adapter and LayoutManager
        mAdapter = new RecipeWidgetConfigureAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.recipe_widget_configure_ac_recipes_recycleview);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this,
                getResources().getInteger(R.integer.recipes_ac_grid_spancount));
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        loadRecipes();
    }


    public void loadRecipes() {
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
        if (!cached) {
            showLoading("Loading...");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_error_button:
                loadRecipes();
                break;
            default:
                throw new UnsupportedOperationException("There is no click for view " + view.getId());
        }
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        final Context context = RecipeWidgetConfigureActivity.this;

        // When the button is clicked, store the recipeId locally
        saveRecipePref(context, mAppWidgetId, recipe.getId());

        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RecipeWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }
}

