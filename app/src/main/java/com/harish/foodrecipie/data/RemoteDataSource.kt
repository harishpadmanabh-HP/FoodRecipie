package com.harish.foodrecipie.data

import com.harish.foodrecipie.data.network.FoodRecipesAPI
import com.harish.foodrecipie.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesAPI: FoodRecipesAPI
) {
    suspend fun getRecipes(queries:Map<String,String>):Response<FoodRecipe>{
      return  foodRecipesAPI.getRecipes(queries)
    }
}