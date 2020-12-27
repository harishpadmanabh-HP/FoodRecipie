package com.harish.foodrecipie.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [RecipesEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(RecipesConvertor::class)
abstract class RecipesDatabase :RoomDatabase(){
    abstract fun recipesDao(): RecipesDao
}