package com.harish.foodrecipie.ui.recipes

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.harish.foodrecipie.MainViewModel
import com.harish.foodrecipie.R
import com.harish.foodrecipie.adapters.RecipesAdapter
import com.harish.foodrecipie.databinding.FragmentRecipiesBinding
import com.harish.foodrecipie.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_food_joke.view.*
import kotlinx.android.synthetic.main.fragment_recipies.*
import kotlinx.android.synthetic.main.fragment_recipies.view.*
import kotlinx.coroutines.launch
import java.util.logging.Logger

@AndroidEntryPoint
class RecipiesFragment : Fragment() {

    private var _binding : FragmentRecipiesBinding ?=null
    private val binding get( )=_binding!!
    private lateinit var mainViewModel: MainViewModel
    //private lateinit var recipesViewModel: RecipesViewModel
    private val mAdapter by lazy { RecipesAdapter() }
    private var mView : View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
       // recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipiesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        mView = binding.root
        binding.mainViewmodel = mainViewModel

        setupRecyclerView()
       readDatabase()
        return binding.root
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipe.observeOnce(viewLifecycleOwner , Observer{ db_recipeList->
                if(db_recipeList .isNotEmpty()){
                    Log.e("recipes_db","reading offline")
                    mAdapter.setData(db_recipeList[0].foodRecipe)
                    hideShimmerEffect()
                }else{
                    requestApiData()
                }
            })
        }
    }

    fun loadDataFromDb(){
        lifecycleScope.launch {
            mainViewModel.readRecipe.observe(viewLifecycleOwner) { db_recipeList->
                if(db_recipeList .isNotEmpty()){
                    Log.e("recipes_db","reading offline")
                    mAdapter.setData(db_recipeList[0].foodRecipe)
                    hideShimmerEffect()
                }
            }
        }
    }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_NUMBER] = "50"
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = "snack"
        queries[QUERY_DIET] = "vegan"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }
    private fun requestApiData() {
        Log.e("recipes_db","reading api data online")

        mainViewModel.getRecipes(applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner) { response ->
            when(response){
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    loadDataFromDb()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading ->{
                    showShimmerEffect()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        mView?.recyclerview?.adapter = mAdapter
        mView?.recyclerview?.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun showShimmerEffect() {
        mView?.recyclerview?.showShimmer()
    }

    private fun hideShimmerEffect() {
        mView?.recyclerview?.hideShimmer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}