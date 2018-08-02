package com.itdeveapps.omar.bakingapp.recipeslist;

import com.itdeveapps.omar.bakingapp.model.Recipe;

import java.util.List;

public class RecipesContracter {
    public interface View {
        void showProgress(boolean show);

        void initData(List<Recipe> recipes);

        void onError( Throwable throwable);
    }

    public interface Presenter {
        void loadRecipes();

        void subscribe();

        void unsubscribe();
    }
}
