package br.com.udacity.baking.service;

import java.util.List;

import br.com.udacity.baking.recipeslist.Recipe;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeService {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";
    private RecipeClient client;

    public RecipeService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        client = retrofit.create(RecipeClient.class);
    }

    public Call<List<Recipe>> getRecipes() {
        return client.getRecipes();
    }
}
