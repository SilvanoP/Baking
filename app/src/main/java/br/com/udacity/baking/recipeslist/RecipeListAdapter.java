package br.com.udacity.baking.recipeslist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.udacity.baking.R;
import br.com.udacity.baking.utils.ListItemClickListener;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipesListViewHolder> {

    private final ListItemClickListener mListener;
    private Context mContext;
    private List<Recipe> mRecipes;

    public RecipeListAdapter(Context context, List<Recipe> recipes, ListItemClickListener listener) {
        this.mContext = context;
        this.mRecipes = recipes;
        this.mListener = listener;
    }

    @Override
    public RecipesListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_recipes, parent, false);
        return new RecipesListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipesListViewHolder holder, int position) {
        holder.bind(mRecipes.get(position));
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    class RecipesListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_item_image)
        ImageView recipeImage;
        @BindView(R.id.recipe_item_name_text)
        TextView nameTextView;

        public RecipesListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        private void bind(Recipe recipe) {
            if (TextUtils.isEmpty(recipe.getImage())) {
                recipeImage.setImageResource(R.drawable.image_placeholder_recipe);
            } else {
                Picasso.with(mContext).load(recipe.getImage())
                        .placeholder(R.drawable.image_placeholder_recipe)
                        .error(R.drawable.image_placeholder_recipe)
                        .into(recipeImage);
            }

            nameTextView.setText(recipe.getName());
        }

        @Override
        public void onClick(View view) {
            Log.i(this.getClass().getSimpleName(), "Item clicked!");
            mListener.onItemClick(getAdapterPosition());
        }
    }
}
