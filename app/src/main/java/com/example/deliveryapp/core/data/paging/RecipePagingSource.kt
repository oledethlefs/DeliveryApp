package com.example.deliveryapp.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.deliveryapp.core.data.model.Diet
import com.example.deliveryapp.core.data.model.Recipe
import com.example.deliveryapp.core.data.model.RecipeCategory
import com.example.deliveryapp.core.data.model.RecipeSortingCategory
import com.example.deliveryapp.core.network.NetworkDataSource
import com.example.deliveryapp.core.network.model.asModel
import okio.IOException
import retrofit2.HttpException

/**
 * A [PagingSource] that handles the paginated loading of [Recipe] data from a [NetworkDataSource].
 *
 *
 * @property network The [NetworkDataSource] used to fetch recipe data.
 */
class RecipePagingSource (
    private val network: NetworkDataSource
) : PagingSource<Int, Recipe>() {

    private var recipeCategory: RecipeCategory = RecipeCategory.STARTER
    private var sortingCategory: RecipeSortingCategory = RecipeSortingCategory.POPULARITY
    private var diets: List<Diet> = emptyList()




    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Recipe> {
        return try {
            val lastRecipeId = params.key
            val diets = diets.map {it.apiKey}

            val recipes = network.getRecipes(
                recipeCategory = recipeCategory.apiKey,
                diets = diets,
                afterId = lastRecipeId,
                sortBy = sortingCategory.apiKey
            )
            LoadResult.Page(
                data = recipes.map { it.asModel() },
                prevKey = if (lastRecipeId == null) null else recipes.firstOrNull()?.id,
                nextKey = if (recipes.isEmpty()) null else recipes.last().id
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Recipe>): Int? {
        return state.anchorPosition?.let { state.closestItemToPosition(it)?.id }
    }

    fun setRecipeCategory(category: RecipeCategory) {
        recipeCategory = category
    }

    fun setSortingCategory(category: RecipeSortingCategory) {
        sortingCategory = category
    }

    fun setDiets(dietList: List<Diet>) {
        diets = dietList
    }

}