package com.harish.foodrecipie.di

import android.content.Context
import androidx.room.Room
import com.harish.foodrecipie.data.database.RecipesDatabase
import com.harish.foodrecipie.util.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabse(@ApplicationContext appContext: Context) = Room.databaseBuilder(
        appContext,
        RecipesDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDao(databse : RecipesDatabase) = databse.recipesDao()

}