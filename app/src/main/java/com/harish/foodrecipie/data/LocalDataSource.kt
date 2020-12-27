package com.harish.foodrecipie.data

import com.harish.foodrecipie.data.database.RecipesDao
import com.harish.foodrecipie.data.database.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
){
     fun readDatabase():Flow<List<RecipesEntity>> = recipesDao.readRecipes()

   suspend  fun insertRecipes(recipesEntity: RecipesEntity){
        recipesDao.insertRecipe(recipesEntity)
    }
}
