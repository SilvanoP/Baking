package br.com.udacity.android.baking.ui.recipe;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.udacity.android.baking.R;
import br.com.udacity.android.baking.data.Ingredient;


public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Ingredient mIngredient;

        private final TextView mQuantity;
        private final TextView mName;

        ViewHolder(View v) {
            super(v);
            mQuantity = v.findViewById(R.id.recipe_ac_ingredient_quantity_textview);
            mName = v.findViewById(R.id.recipe_ac_ingredient_name_textview);
            v.setOnClickListener(this);
        }

        void updateData(int index, Ingredient ingredient) {
            mIngredient = ingredient;
            mQuantity.setText(mIngredient.getFormattedQuantity());
            mName.setText(mIngredient.getIngredient());
        }

        @Override
        public void onClick(View view) {
            mOnClickIngredientListener.onIngredientClick(mIngredient);
        }
    }


    public interface OnIngredientClickListener {
        void onIngredientClick(Ingredient ingredient);
    }

    private List<Ingredient> mIngredients;
    private OnIngredientClickListener mOnClickIngredientListener;

    IngredientAdapter(OnIngredientClickListener listener) {
        this.mOnClickIngredientListener = listener;
    }

    void setIngredients(List<Ingredient> ingredients) {
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    @Override
    public IngredientAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_recipe_ingredients_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Ingredient ingredient = mIngredients.get(position);
        holder.updateData(position, ingredient);
    }

    @Override
    public int getItemCount() {
        return mIngredients == null ? 0 : mIngredients.size();
    }

}
