package com.harish.foodrecipie

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.harish.foodrecipie.models.FoodRecipe
import com.harish.foodrecipie.util.RECIPES_TABLE


@Entity(tableName = RECIPES_TABLE)
class RecipesEntity    (
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}