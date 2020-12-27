package com.harish.foodrecipie.bindingadapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.room.Database
import com.harish.foodrecipie.data.database.RecipesEntity
import com.harish.foodrecipie.models.FoodRecipe
import com.harish.foodrecipie.util.NetworkResult

class RecipesBinding {
    companion object {

        @JvmStatic
        @BindingAdapter("readapi", "readDatabase",requireAll = true)
        fun errorImageViewVisibilty(
            view: ImageView,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipesEntity>?
        ) {
            if(apiResponse is NetworkResult.Error && database.isNullOrEmpty()){
                view.visibility = View.VISIBLE
            }else if (apiResponse is NetworkResult.Loading){
                view.visibility = View.INVISIBLE
            }else if(apiResponse is NetworkResult.Success){
                    view.visibility = View.INVISIBLE
                }
        }

        @JvmStatic
        @BindingAdapter("tv_readapi", "tv_readDatabase",requireAll = true)
        fun errorTextviewVisibilty(
            view:TextView,
            apiResponse: NetworkResult<FoodRecipe>?,
            database: List<RecipesEntity>?
        ){
            if(apiResponse is NetworkResult.Error && database.isNullOrEmpty()){
                view.visibility = View.VISIBLE
                view.text = apiResponse.message.toString()
            }else if (apiResponse is NetworkResult.Loading){
                view.visibility = View.INVISIBLE
            }else if(apiResponse is NetworkResult.Success){
                view.visibility = View.INVISIBLE
            }
        }
    }
}