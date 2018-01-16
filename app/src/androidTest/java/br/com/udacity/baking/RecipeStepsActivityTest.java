package br.com.udacity.baking;

import android.support.test.espresso.ViewInteraction;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.udacity.android.baking.R;
import br.com.udacity.android.baking.ui.recipes.RecipesActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class RecipeStepsActivityTest {

    @Rule
    public ActivityTestRule<RecipesActivity> mActivityTestRule = new ActivityTestRule<RecipesActivity>(RecipesActivity.class);

    @Test
    public void recipeStepsActivityTest() {
        ViewInteraction recipesList = onView(withId(R.id.recipes_ac_recycleview));
        recipesList.perform(actionOnItemAtPosition(1, click()));
        sleep(1000);

        // click at first step
        ViewInteraction stepsList = onView(
                withId(R.id.recipe_ac_step_list));
        stepsList.perform(actionOnItemAtPosition(0, click()));
        sleep(1000);

        // check if left was hide
        onView(withId(R.id.step_ac_nav_left)).check(matches(not(isDisplayed())));
        // check if right was shown
        onView(withId(R.id.step_ac_nav_right)).check(matches(isDisplayed()));

        for (int i = 0; i < 9; i++) {
            navToRight();
        }

        // check if left was shown
        onView(withId(R.id.step_ac_nav_left)).check(matches(isDisplayed()));
        // check if right was hide
        onView(withId(R.id.step_ac_nav_right)).check(matches(not(isDisplayed())));
    }

    private void navToRight() {
        ViewInteraction floatingActionButton = onView(withId(R.id.step_ac_nav_right));
        floatingActionButton.perform(click());

        sleep(1000);
    }

    private void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
