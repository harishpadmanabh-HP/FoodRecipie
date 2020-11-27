package com.harish.foodrecipie.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.harish.foodrecipie.databinding.ReciepiesRowLayoutBinding
import com.harish.foodrecipie.models.FoodRecipe
import com.harish.foodrecipie.models.Result
import com.harish.foodrecipie.util.RecipeDiffUtil

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>() {

    private var recipes = emptyList<Result>()

    class RecipeViewHolder(private val binding: ReciepiesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result) {
            binding.result = result
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RecipeViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ReciepiesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return RecipeViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        return RecipeViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val currentRecipe = recipes[position]
        holder.bind(currentRecipe)
    }

    fun setData(newData: FoodRecipe) {
        val recipeDiffUtil = RecipeDiffUtil(recipes, newData.results)
        val diffResult = DiffUtil.calculateDiff(recipeDiffUtil)
        recipes = newData.results
        diffResult.dispatchUpdatesTo(this)
    }
}