package br.com.udacity.baking.service;

import java.util.List;

import br.com.udacity.baking.recipeslist.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeClient {

    @GET("/topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();
}
