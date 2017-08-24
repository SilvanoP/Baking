package br.com.udacity.baking.recipedetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.udacity.baking.R;
import br.com.udacity.baking.utils.Utils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {

    private Context mContext;
    private List<Ingredient> mIngredients;

    public IngredientsAdapter(Context context, List<Ingredient> ingredients) {
        mContext = context;
        mIngredients = ingredients;
    }

    @Override
    public IngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_ingredients, parent, false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientsViewHolder holder, int position) {
        holder.bind(mIngredients.get(position));
    }

    @Override
    public int getItemCount() {
        return mIngredients.size();
    }

    class IngredientsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ingredient_name_text)
        TextView nameTextView;
        @BindView(R.id.ingredient_measure_text)
        TextView measureTextView;
        @BindView(R.id.ingredient_quantity_text)
        TextView quantityTextView;

        public IngredientsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(Ingredient ingredient) {
            nameTextView.setText(ingredient.getIngredient());
            String measureText = mContext.getResources().getString(R.string.measure)
                    + " " + mContext.getResources().getString(Utils.getMeasureResource(ingredient.getMeasure()));
            measureTextView.setText(measureText);
            String quantityText = mContext.getResources().getString(R.string.quantity)
                    + " " + String.valueOf(ingredient.getQuantity());
            quantityTextView.setText(quantityText);
        }
    }
}
