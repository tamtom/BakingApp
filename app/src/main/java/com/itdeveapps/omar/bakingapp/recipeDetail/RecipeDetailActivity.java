package com.itdeveapps.omar.bakingapp.recipeDetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.itdeveapps.omar.bakingapp.R;
import com.itdeveapps.omar.bakingapp.model.Recipe;
import com.itdeveapps.omar.bakingapp.stepDetail.StepDetailActivity;
import com.itdeveapps.omar.bakingapp.stepDetail.StepDetailFragment;

import org.parceler.Parcels;

import static com.itdeveapps.omar.bakingapp.stepDetail.StepDetailActivity.EXTRA_RECIPE_NAME;
import static com.itdeveapps.omar.bakingapp.stepDetail.StepDetailActivity.EXTRA_STEPS;
import static com.itdeveapps.omar.bakingapp.stepDetail.StepDetailActivity.EXTRA_STEP_INDEX;

public class RecipeDetailActivity extends AppCompatActivity implements
        StepsFragment.OnInteractionListener,
        StepDetailFragment.OnInteractionListener {

    public static final String EXTRA_RECIPE = "recipe";
    private static final int REQUEST_CODE_STEP_DETAIL = 1;
    Recipe recipe;
    boolean isTwoPane;
    int selectedStepIndex = 0;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        recipe = Parcels.unwrap(getIntent().getExtras().getParcelable(EXTRA_RECIPE));
        isTwoPane = getResources().getBoolean(R.bool.isTablet);

        getSupportActionBar().setTitle(recipe.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {

            fragmentManager.beginTransaction()
                    .add(R.id.ingredientsContainerFl, IngredientsFragment.newInstance(recipe.getIngredients()))
                    .commit();

            fragmentManager.beginTransaction()
                    .add(R.id.stepsContainerFl, StepsFragment.newInstance(recipe.getSteps()))
                    .commit();

            if (isTwoPane) {
                fragmentManager.beginTransaction()
                        .add(R.id.stepDetailContainerFl, StepDetailFragment.newInstance(recipe.getSteps(), selectedStepIndex))
                        .commit();
            }
        }

    }

    @Override
    public void onStepClicked(int stepIndex) {
        if (isTwoPane) {
            fragmentManager.beginTransaction()
                    .replace(R.id.stepDetailContainerFl, StepDetailFragment.newInstance(recipe.getSteps(), stepIndex))
                    .commit();
        } else {
            startActivityForResult(new Intent(this, StepDetailActivity.class)
                    .putExtra(EXTRA_STEPS, Parcels.wrap(recipe.getSteps()))
                    .putExtra(EXTRA_RECIPE_NAME, recipe.getName())
                    .putExtra(EXTRA_STEP_INDEX, stepIndex), REQUEST_CODE_STEP_DETAIL);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_STEP_DETAIL) {
            Fragment fragment = fragmentManager.findFragmentById(R.id.stepsContainerFl);
            ((StepsFragment) fragment).resetStep();
        }
    }

    @Override
    public void onPreviousStepClicked(int stepIndex) {
        if (isTwoPane) {
            fragmentManager.beginTransaction()
                    .replace(R.id.stepDetailContainerFl, StepDetailFragment.newInstance(recipe.getSteps(), stepIndex - 1))
                    .commit();

            Fragment fragment = fragmentManager.findFragmentById(R.id.stepsContainerFl);
            ((StepsFragment) fragment).setSelectedStep(stepIndex-1);
        }
    }

    @Override
    public void onNextStepClicked(int stepIndex) {
        if (isTwoPane) {
            fragmentManager.beginTransaction()
                    .replace(R.id.stepDetailContainerFl, StepDetailFragment.newInstance(recipe.getSteps(), stepIndex + 1))
                    .commit();

            Fragment fragment = fragmentManager.findFragmentById(R.id.stepsContainerFl);
            ((StepsFragment) fragment).setSelectedStep(stepIndex+1);
        }
    }
}
