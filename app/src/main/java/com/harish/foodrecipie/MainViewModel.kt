package com.harish.foodrecipie

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.harish.foodrecipie.data.Repository
import com.harish.foodrecipie.models.FoodRecipe
import com.harish.foodrecipie.util.NetworkResult
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception


class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    app : Application
) : AndroidViewModel(app){

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
            }catch (e:Exception){
                recipesResponse.value =    NetworkResult.Error("Recipe not found")
            }
    }else{
            recipesResponse.value = NetworkResult.Error("No internet connection ")
        }
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