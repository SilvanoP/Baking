package br.com.udacity.android.baking.data.source.remote;

import java.util.List;

import br.com.udacity.android.baking.data.Recipe;
import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingServices {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();

}
