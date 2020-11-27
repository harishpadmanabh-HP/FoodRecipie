package com.harish.foodrecipie.bindingadapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.harish.foodrecipie.R

class RecipeRowBinding{

    companion object{

        @BindingAdapter("setLikes")
        @JvmStatic
        fun setNumberOfLikes(view : TextView,likes : Int){
            view.text = likes.toString()
        }
        @BindingAdapter("setVegan")
        @JvmStatic
        fun applyVegan(view: View, vegan:Boolean){
            if(vegan){
                when(view){
                    is TextView -> {
                        view.setTextColor(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                    is ImageView -> {
                        view.setColorFilter(
                            ContextCompat.getColor(
                                view.context,
                                R.color.green
                            )
                        )
                    }
                }
            }
        }

        @BindingAdapter("loadImage")
        @JvmStatic
        fun loadImageFromUrl(view:ImageView,url : String){
            view.load(url){
                crossfade(600)
            }
        }
    }

}