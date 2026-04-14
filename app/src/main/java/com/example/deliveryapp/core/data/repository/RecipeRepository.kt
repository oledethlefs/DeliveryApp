package com.example.deliveryapp.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.deliveryapp.BuildConfig
import com.example.deliveryapp.core.data.model.Diet
import com.example.deliveryapp.core.data.model.Recipe
import com.example.deliveryapp.core.data.model.RecipeCategory
import com.example.deliveryapp.core.data.model.RecipeSortingCategory
import com.example.deliveryapp.core.data.paging.RecipePagingSource
import com.example.deliveryapp.core.network.NetworkDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Repository responsible for managing the retrieval of recipe data from the network.
 *
 * This class acts as a mediator between the data source and the UI layer, providing
 * paginated recipe data filtered by category and dietary preferences.
 *
 * @property network The [NetworkDataSource] used to fetch data from the remote API.
 */
class RecipeRepository @Inject constructor(
    private val network: NetworkDataSource
) {
    /**
     * Retrieves a paginated stream of recipes based on the specified category, dietary filters, and sorting criteria.
     *
     * @param recipeCategory The category of recipes to retrieve.
     * @param diets A list of dietary preferences or restrictions to filter the results.
     * @param sortingCategory The criteria used to sort the returned recipes.
     * @return A [Flow] emitting [PagingData] of [Recipe] objects.
     */
    fun getRecipes(
        recipeCategory: RecipeCategory,
        diets: List<Diet>,
        sortingCategory: RecipeSortingCategory): Flow<PagingData<Recipe>> {
        val pagingSourceFactory = {
            RecipePagingSource(network).apply {
                setRecipeCategory(recipeCategory)
                setDiets(diets)
                setSortingCategory(sortingCategory)

            }
        }
        return Pager(
            config = PagingConfig(pageSize = BuildConfig.MAX_PAGE_SIZE),
            pagingSourceFactory = pagingSourceFactory
        ).flow

    }
}