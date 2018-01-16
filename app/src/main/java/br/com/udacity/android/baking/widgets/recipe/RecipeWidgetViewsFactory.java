package br.com.udacity.android.baking.widgets.recipe;

import android.content.Context;
import android.widget.RemoteViews;

import java.util.List;

import br.com.udacity.android.baking.R;
import br.com.udacity.android.baking.data.Ingredient;
import br.com.udacity.android.baking.data.source.RecipesRepository;
import br.com.udacity.android.baking.util.Inject;


public class RecipeWidgetViewsFactory implements RecipeWidgetService.RemoteViewsFactory {

    private int mAppWidgetId;
    private int mRecipeId;
    private Context mContext;
    private List<Ingredient> mIngredients;

    public RecipeWidgetViewsFactory(Context context, int appWidgetId, int recipeId) {
        mContext = context;
        mAppWidgetId = appWidgetId;
        mRecipeId = recipeId;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        RecipesRepository recipesRepository = Inject.getRecipeRepository(mContext);
        mIngredients = recipesRepository.getIngredients(mRecipeId);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mIngredients == null ? 0 : mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(mContext.getPackageName(),
                R.layout.recipe_widget_ingredient_item);

        Ingredient ingredient = mIngredients.get(position);
        row.setTextViewText(R.id.recipe_wd_ingredient_item_quantity_textview, ingredient.getFormattedQuantity());
        row.setTextViewText(R.id.recipe_wd_ingredient_item_ingredient_textview, ingredient.getIngredient());
        return (row);
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
