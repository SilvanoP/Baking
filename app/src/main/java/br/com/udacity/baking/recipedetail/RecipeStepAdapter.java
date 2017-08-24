package br.com.udacity.baking.recipedetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepViewHolder> {

    private Context mContext;
    private ListItemClickListener mListener;
    private List<RecipeStep> mSteps;

    public RecipeStepAdapter(Context context, ListItemClickListener listener, List<RecipeStep> steps) {
        this.mContext = context;
        this.mListener = listener;
        this.mSteps = steps;
    }

    @Override
    public RecipeStepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.list_item_step, parent, false);
        return new RecipeStepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeStepViewHolder holder, int position) {
        holder.bind(mSteps.get(position));
    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    class RecipeStepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.step_shot_text)
        TextView shortTextView;
        @BindView(R.id.step_description_text)
        TextView descTextView;

        public RecipeStepViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        void bind(RecipeStep recipeStep) {
            shortTextView.setText(recipeStep.getShortDescription());
            descTextView.setText(recipeStep.getDescription());
        }

        @Override
        public void onClick(View view) {
            mListener.onItemClick(getAdapterPosition());
        }
    }
}
