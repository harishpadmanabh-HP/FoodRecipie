package com.harish.foodrecipie.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE )
    suspend fun insertRecipe(recipesEntity: RecipesEntity)

    @Query("select * from recipes_tb order by id asc")
    fun readRecipes() : Flow<List<RecipesEntity>>
}