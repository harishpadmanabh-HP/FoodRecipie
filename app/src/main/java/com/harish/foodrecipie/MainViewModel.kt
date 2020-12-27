package com.harish.foodrecipie

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.harish.foodrecipie.data.Repository
import com.harish.foodrecipie.data.database.RecipesEntity
import com.harish.foodrecipie.models.FoodRecipe
import com.harish.foodrecipie.util.NetworkResult
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception


class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    app : Application
) : AndroidViewModel(app){

    /** ROOM */
    val readRecipe : LiveData<List<RecipesEntity>> = repository.local.readDatabase().asLiveData()

    fun insertRecipes(recipesEntity: RecipesEntity) = viewModelScope.launch (IO){
        Log.e("recipes_db","insert called")
        repository.local.insertRecipes(recipesEntity)
    }




    //-------RETROFIT--------------------//
    var recipesResponse : MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(querries : Map<String,String>) = viewModelScope.launch {
        getRecipesSafeCall(querries)
    }

    private suspend fun getRecipesSafeCall(querries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if(hasInternet()){
            try{



               val response = repository.remote.getRecipes(querries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                //offline
                val foodRecipe = recipesResponse.value!!.data
               if(foodRecipe != null)
                   offlineRecipes(foodRecipe)
            }catch (e:Exception){
                Log.e("recipes_db","safe call exc caught $e")
                recipesResponse.value =    NetworkResult.Error("Recipe not found")
            }
    }else{
            recipesResponse.value = NetworkResult.Error("No internet connection ")
        }
}

    private fun offlineRecipes(foodRecipe: FoodRecipe) {
           val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {

        when{
            response.message().toString().contains("timeout") ->{
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("Api key limited")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes Not found")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternet():Boolean{
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when{
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false

        }
    }

}