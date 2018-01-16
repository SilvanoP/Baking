package br.com.udacity.android.baking.ui.recipe;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import br.com.udacity.android.baking.data.Step;

public class StepPagerAdapter extends FragmentStatePagerAdapter {

    private List<Step> mSteps;

    StepPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    void setSteps(List<Step> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }


    @Override
    public StepFragment getItem(int position) {
        Step step = mSteps.get(position);
        return StepFragment.newInstance(step.getRecipeId(), step.getId());
    }

    @Override
    public int getCount() {
        return mSteps == null ? 0 : mSteps.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mSteps.get(position).getShortDescription();
    }
}
