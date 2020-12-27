package com.harish.foodrecipie.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.harish.foodrecipie.models.FoodRecipe

class RecipesConvertor {

    @TypeConverter
    fun recipeToString(foodRecipe: FoodRecipe):String
    {
        return Gson().toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToRecipe(data : String):FoodRecipe{
        val listType = object : TypeToken<FoodRecipe>() {}.type
        return Gson().fromJson(data, listType)
    }
}