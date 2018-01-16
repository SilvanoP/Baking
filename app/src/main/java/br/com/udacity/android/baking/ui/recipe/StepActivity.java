package br.com.udacity.android.baking.ui.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import br.com.udacity.android.baking.R;
import br.com.udacity.android.baking.data.Step;
import br.com.udacity.android.baking.data.source.RecipesDataSource;
import br.com.udacity.android.baking.data.source.RecipesRepository;
import br.com.udacity.android.baking.ui.BaseActivity;
import br.com.udacity.android.baking.util.Inject;


public class StepActivity extends BaseActivity implements View.OnClickListener, StepFragment.OnPlayerControlsVisibilityChangeListener {

    private static final String ARG_RECIPE_ID = "recipe_id";
    private static final String ARG_STEP_ID = "step_id";

    private int mRecipeId;
    private int mStepId;
    private List<Step> mSteps;

    private RecipesRepository mRecipesRepository;
    private StepPagerAdapter mStepPagerAdapter;
    private ViewPager mViewPager;
    private FloatingActionButton mNavLeft;
    private FloatingActionButton mNavRight;

    private int mCurrentItem = 0;
    private boolean mIsShowingControls = true;

    public static Intent getStartIntent(Context context, Step step) {
        Intent intent = new Intent(context, StepActivity.class);
        intent.putExtra(ARG_RECIPE_ID, step.getRecipeId());
        intent.putExtra(ARG_STEP_ID, step.getId());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        mRecipeId = getIntent().getIntExtra(ARG_RECIPE_ID, -1);
        mStepId = getIntent().getIntExtra(ARG_STEP_ID, -1);
        mRecipesRepository = Inject.getRecipeRepository(this);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // ViewPager and its adapters use support library
        // fragments, so use getSupportFragmentManager.
        mStepPagerAdapter = new StepPagerAdapter(
                getSupportFragmentManager());
        mViewPager = findViewById(R.id.step_ac_steps_viewpager);
        mViewPager.setAdapter(mStepPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.e("SPRENGEL", "onPageScrolled(int "+position+", float "+positionOffset+", int "+positionOffsetPixels+")");
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("SPRENGEL", "onPageSelected: mCurrentItem " + mCurrentItem);
                mStepPagerAdapter.getItem(mCurrentItem).losingVisibility();
                Log.e("SPRENGEL", "onPageSelected" + position);
                mCurrentItem = position;
                updateNavControls();
                getSupportActionBar().setTitle(mSteps.get(position).getShortDescription());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.e("SPRENGEL", "onPageScrollStateChanged" + state);
            }
        });
        mViewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            private static final float MIN_SCALE = 0.85f;
            private static final float MIN_ALPHA = 0.5f;

            @Override
            public void transformPage(@NonNull View page, float position) {
                int pageWidth = page.getWidth();
                int pageHeight = page.getHeight();

                View view = page; //page.findViewById(R.id.step_frag_description_textview);

                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.setAlpha(0);

                } else if (position <= 1) { // [-1,1]
                    // Modify the default slide transition to shrink the page as well
                    float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                    float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                    float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                    if (position < 0) {
                        view.setTranslationX(horzMargin - vertMargin / 2);
                    } else {
                        view.setTranslationX(-horzMargin + vertMargin / 2);
                    }

                    // Scale the page down (between MIN_SCALE and 1)
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);

                    // Fade the page relative to its size.
                    view.setAlpha(MIN_ALPHA +
                            (scaleFactor - MIN_SCALE) /
                                    (1 - MIN_SCALE) * (1 - MIN_ALPHA));
                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0);
                }
            }
        });

        mNavLeft = findViewById(R.id.step_ac_nav_left);
        mNavRight = findViewById(R.id.step_ac_nav_right);
        mNavLeft.setOnClickListener(this);
        mNavRight.setOnClickListener(this);

        loadSteps();
    }

    private void loadSteps() {
        mRecipesRepository.getSteps(mRecipeId, new RecipesDataSource.LoadStepsCallback() {
            @Override
            public void onStepsLoaded(List<Step> steps) {
                mSteps = steps;
                mStepPagerAdapter.setSteps(mSteps);
                for (int position = 0; position < mSteps.size(); position++) {
                    Step step = mSteps.get(position);
                    if (step.getId() == mStepId) {
                        mCurrentItem = position;
                        mViewPager.setCurrentItem(mCurrentItem, true);

                        // It's just to update the ui for the first item.
                        // the ViewPager.OnPageChangeListener is not called for the first step.
                        updateNavControls();
                        getSupportActionBar().setTitle(mSteps.get(position).getShortDescription());
                    }
                }
            }

            @Override
            public void onDataNotAvailable() {
                Snackbar.make(mViewPager, getString(R.string.internal_error), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        int currentItem = mViewPager.getCurrentItem();
        switch (view.getId()) {
            case R.id.step_ac_nav_left:
                mViewPager.setCurrentItem(--currentItem, true);
                break;
            case R.id.step_ac_nav_right:
                mViewPager.setCurrentItem(++currentItem, true);
                break;
            default:
                throw new UnsupportedOperationException("Invalid action to viewId: " + view.getId());
        }
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        if (getResources().getBoolean(R.bool.step_ac_fullscreen)) {
            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mViewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    }

    @Override
    public void onPlayerControlesVisibilityChanged(boolean isShowingControls) {
        if (!getResources().getBoolean(R.bool.step_ac_fullscreen)) {
            return;
        }

        mIsShowingControls = isShowingControls;
        updateNavControls();
    }

    private void updateNavControls() {
        if (!mIsShowingControls) {
            mNavLeft.setVisibility(View.GONE);
            mNavRight.setVisibility(View.GONE);
        } else {
            mNavLeft.setVisibility(mCurrentItem == 0 ? View.GONE : View.VISIBLE);
            mNavRight.setVisibility(mCurrentItem == mSteps.size() - 1 ? View.GONE : View.VISIBLE);
        }
    }
}
