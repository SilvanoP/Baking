package br.com.udacity.android.baking.ui.recipes;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.udacity.android.baking.R;
import br.com.udacity.android.baking.data.Recipe;


public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {


    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    private List<Recipe> mRecipes;
    private OnRecipeClickListener mOnClickRecipeListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Recipe mRecipe;
        final ImageView mImage;
        final TextView mName;
        final TextView mServings;

        ViewHolder(View v) {
            super(v);
            mImage = v.findViewById(R.id.recipes_ac_image_imageview);
            mName = v.findViewById(R.id.recipes_ac_name_textview);
            mServings = v.findViewById(R.id.recipes_ac_servings_textview);
            v.setOnClickListener(this);
        }

        void updateData(Recipe recipe) {
            mRecipe = recipe;
            if(!TextUtils.isEmpty(mRecipe.getImage())) {
                Picasso.with(mImage.getContext())
                        .load(recipe.getImage())
                        .placeholder(R.drawable.placeholder_rev)
                        .into(mImage);
            }
            mName.setText(mRecipe.getName());
            mServings.setText(mServings.getContext().getString(R.string.recipes_ac_servings, mRecipe.getServings()));
        }

        @Override
        public void onClick(View view) {
            RecipesAdapter.this.mOnClickRecipeListener.onRecipeClick(mRecipe);
        }
    }

    RecipesAdapter(OnRecipeClickListener listener) {
        this.mOnClickRecipeListener = listener;
    }

    void setRecipes(List<Recipe> recipes) {
        this.mRecipes = recipes;
        notifyDataSetChanged();
    }

    @Override
    public RecipesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_recipes_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);

        holder.updateData(recipe);
    }

    @Override
    public int getItemCount() {
        return mRecipes == null ? 0 : mRecipes.size();
    }

}