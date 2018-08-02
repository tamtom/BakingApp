package com.itdeveapps.omar.bakingapp.recipeslist;

import com.itdeveapps.omar.bakingapp.api.RetrofitWrapper;
import com.itdeveapps.omar.bakingapp.model.Recipe;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class RecipesPresenter implements RecipesContracter.Presenter {
    final CompositeDisposable mCompositeDisposable;
    final RetrofitWrapper mRetrofitWrapper;
    final RecipesContracter.View mView;

    public RecipesPresenter(RecipesContracter.View view) {
        mCompositeDisposable = new CompositeDisposable();
        mRetrofitWrapper = RetrofitWrapper.getInstance();
        mView = view;
    }

    @Override
    public void loadRecipes() {
        mView.showProgress(true);
        mCompositeDisposable.add(mRetrofitWrapper
                .getApiService().getRecipes()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Recipe>>() {

                    @Override
                    public void onNext(List<Recipe> recipes) {
                        mView.showProgress(false);
                        mView.initData(recipes);

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(e);
                    }

                    @Override
                    public void onComplete() {
                    }
                }));

    }

    @Override
    public void subscribe() {
        loadRecipes();
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }
}
