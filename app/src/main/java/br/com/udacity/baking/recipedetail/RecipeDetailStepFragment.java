package br.com.udacity.baking.recipedetail;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.udacity.baking.R;

public class RecipeDetailStepFragment extends Fragment {

    private RecipeStep mStep;
    private int mStepIndex;

    public RecipeDetailStepFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipe_detail_step, container, false);
    }

    public void setAttributes(RecipeStep step, int index) {
        mStep = step;
        mStepIndex = index;
    }

}
