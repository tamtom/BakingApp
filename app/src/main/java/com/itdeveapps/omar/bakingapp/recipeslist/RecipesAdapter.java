package com.itdeveapps.omar.bakingapp.recipeslist;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itdeveapps.omar.bakingapp.R;
import com.itdeveapps.omar.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {

    List<Recipe> recipes;
    ItemListener itemListener;

    interface ItemListener {
        void onItemClick(Recipe recipe);
    }

    public RecipesAdapter(List<Recipe> recipes, ItemListener itemListener) {
        this.recipes = recipes;
        this.itemListener = itemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindItem(recipes.get(position));
    }

    @Override
    public int getItemCount() {
        return recipes == null ? 0 : recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recipeNameTv)
        TextView recipeNameTv;
        @BindView(R.id.simpleTagIv)
        ImageView ivRecipe;

        @OnClick(R.id.recipeContainerCv)
        public void onItemClicked() {
            itemListener.onItemClick(recipes.get(getAdapterPosition()));
        }

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindItem(Recipe recipe) {
            recipeNameTv.setText(recipe.getName());
            if (!TextUtils.isEmpty(recipe.getImage()))
                Picasso.with(itemView.getContext()).load(recipe.getImage())
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_placeholder)
                        .into(ivRecipe);
        }
    }
}