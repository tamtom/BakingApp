package com.itdeveapps.omar.bakingapp.recipeslist;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.constraint.ConstraintLayout;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itdeveapps.omar.bakingapp.ErrorUtils;
import com.itdeveapps.omar.bakingapp.IdelResourcingImpl;
import com.itdeveapps.omar.bakingapp.R;
import com.itdeveapps.omar.bakingapp.model.Recipe;
import com.itdeveapps.omar.bakingapp.recipeDetail.RecipeDetailActivity;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeesActivity extends AppCompatActivity implements RecipesContracter.View, RecipesAdapter.ItemListener {
    private static final String TAG = RecipeesActivity.class.getSimpleName();
    @BindView(R.id.recipeRv)
    RecyclerView recipeRv;

    @BindView(R.id.messageLayout)
    ConstraintLayout messageLayout;

    @BindView(R.id.messageTv)
    TextView messageTv;

    @BindView(R.id.loadingPw)
    ProgressBar loadingPw;

    @BindView(R.id.retryBtn)
    Button retryBtn;
    @Nullable
    private IdelResourcingImpl mIdlingResource;
    private RecipesContracter.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        ButterKnife.bind(this);
        mPresenter = new RecipesPresenter(this);
        initIdleResouce();
        mPresenter.subscribe();

    }

    void initIdleResouce() {
        getIdlingResource();
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(false);
        }

    }

    @OnClick(R.id.retryBtn)
    void onRetryBtnClicked() {
        mPresenter.loadRecipes();
    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new IdelResourcingImpl();
        }
        return mIdlingResource;
    }

    @Override
    public void showProgress(boolean show) {
        loadingPw.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void initData(List<Recipe> recipes) {
        int spanCount = getResources().getInteger(R.integer.spanCount);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        recipeRv.setLayoutManager(gridLayoutManager);
        recipeRv.setVisibility(View.VISIBLE);
        messageLayout.setVisibility(View.GONE);
        RecipesAdapter adapter = new RecipesAdapter(recipes, this);
        recipeRv.setAdapter(adapter);
        if (mIdlingResource != null) {
            mIdlingResource.setIdleState(true);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (throwable != null) {
            showError(throwable);
            if (mIdlingResource != null) {
                mIdlingResource.setIdleState(true);
            }
        } else messageLayout.setVisibility(View.GONE);
    }

    private void showError(Throwable throwable) {
        recipeRv.setVisibility(View.GONE);
        messageLayout.setVisibility(View.VISIBLE);
        retryBtn.setVisibility(View.VISIBLE);
        messageTv.setText(ErrorUtils.displayFriendlyErrorMessage(this, throwable));
    }

    @Override
    public void onItemClick(Recipe recipe) {
        startActivity(new Intent(this, RecipeDetailActivity.class)
                .putExtra(RecipeDetailActivity.EXTRA_RECIPE, Parcels.wrap(recipe)));

    }
}
