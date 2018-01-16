package br.com.udacity.android.baking.widgets.recipe;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import br.com.udacity.android.baking.R;
import br.com.udacity.android.baking.data.Recipe;
import br.com.udacity.android.baking.data.source.RecipesDataSource;
import br.com.udacity.android.baking.data.source.RecipesRepository;
import br.com.udacity.android.baking.util.Inject;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link RecipeWidgetConfigureActivity RecipeWidgetConfigureActivity}
 */
public class RecipeWidget extends AppWidgetProvider {

    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        int recipeId = RecipeWidgetConfigureActivity.loadRecipePref(context, appWidgetId);
        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);

        RecipesRepository recipesRepository = Inject.getRecipeRepository(context);
        recipesRepository.getRecipe(recipeId, new RecipesDataSource.LoadRecipeCallback() {
            @Override
            public void onRecipeLoaded(Recipe recipe) {
                views.setTextViewText(R.id.recipe_wd_recipe_name_textview, recipe.getName());
                appWidgetManager.partiallyUpdateAppWidget(appWidgetId, views);
            }

            @Override
            public void onDataNotAvailable() {
                views.setTextViewText(R.id.recipe_wd_recipe_name_textview, context.getResources().getString(R.string.not_available_data));
                appWidgetManager.partiallyUpdateAppWidget(appWidgetId, views);
            }
        });

        Intent intent = RecipeWidgetService.getRemoteAdapterIntent(context, appWidgetId, recipeId);
        views.setRemoteAdapter(R.id.recipe_wd_ingredients_listview, intent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);


    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            RecipeWidgetConfigureActivity.deleteRecipePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

