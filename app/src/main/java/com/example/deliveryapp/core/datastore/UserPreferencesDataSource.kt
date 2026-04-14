package com.example.deliveryapp.core.datastore


import android.util.Log
import androidx.datastore.core.DataStore
import com.example.deliveryapp.core.data.model.Diet
import com.example.deliveryapp.core.data.model.UserData
import kotlinx.coroutines.flow.map
import java.io.IOException

import javax.inject.Inject

/**
 * DataStore for storing and retrieving [UserData].
 */
class UserPreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>
) {
    /**
     * Stream of [UserData].
     */
    val userData = userPreferences.data
        .map {
            UserData(
                isVegan = it.isVegan,
                isVegetarian = it.isVegetarian,
                isGlutenFree = it.isGlutenFree,
                isDairyFree = it.isDairyFree,
                isLowCarb = it.isLowCarb,
                isOrganic = it.isOrganic,
                isHalal = it.isHalal,
                isPescetarian = it.isPescetarian,
                isGmoFree = it.isGmoFree,
                userId = it.userId,
                favoriteProductsIds = it.favoriteProductsIdsList.toList(),
                bookmarkedRecipeIds = it.bookmarkedRecipeIdsList.toList()
            )
        }


    /**
     * Updates the user's dietary preferences in the [DataStore].
     *
     */
    suspend fun updateDiets(diets: List<Diet>) {
        userPreferences.updateData {
            it.toBuilder().apply {
                isVegan = diets.contains(Diet.VEGAN)
                isVegetarian = diets.contains(Diet.VEGETARIAN)
                isGlutenFree = diets.contains(Diet.GLUTEN_FREE)
                isDairyFree = diets.contains(Diet.DAIRY_FREE)
                isLowCarb = diets.contains(Diet.LOW_CARB)
                isOrganic = diets.contains(Diet.ORGANIC)
                isHalal = diets.contains(Diet.HALAL)
                isPescetarian = diets.contains(Diet.PESCETARIAN)
                isGmoFree = diets.contains(Diet.GMO_FREE)
            }.build()
        }
    }


    /**
     * Sets the user ID in the preferences.
     *
     * @param userId The unique identifier for the user.
     */
    suspend fun setUserId(userId: Int) {
        userPreferences.updateData {
            it.toBuilder().setUserId(userId).build()
        }
    }


    /**
     * Toggles the bookmarked state for a given recipe. If the recipe is already bookmarked,
     * it will be removed; otherwise, it will be added to the bookmarks.
     *
     * @param recipeId The unique identifier of the recipe to be bookmarked or unbookmarked.
     */
    suspend fun setRecipeBookmarked(recipeId: Int) {
        try {
            userPreferences.updateData {
                val currentBookmarkedRecipes = it.bookmarkedRecipeIdsList.toMutableList()
                if (recipeId in currentBookmarkedRecipes) currentBookmarkedRecipes.remove(recipeId) else currentBookmarkedRecipes.add(
                    recipeId
                )
                it.toBuilder()
                    .clearBookmarkedRecipeIds()
                    .addAllBookmarkedRecipeIds(currentBookmarkedRecipes)
                    .build()

            }
        } catch (ioException: IOException) {
            Log.e("DataStore", "Failed to update bookmarked recipes", ioException)
        }
    }

    /**
     * Toggles the favorite status of a specific product. If the product is already a favorite,
     * it will be removed; otherwise, it will be added to the list of favorite products.
     *
     * @param productId The unique identifier of the product to be marked as a favorite.
     *
     */
    suspend fun setProductFavorite(productId: Int) {
        try {
            userPreferences.updateData {
                val currentFavoriteProducts = it.favoriteProductsIdsList.toMutableList()
                if (productId in currentFavoriteProducts) currentFavoriteProducts.remove(productId) else currentFavoriteProducts.add(
                    productId
                )
                it.toBuilder()
                    .clearFavoriteProductsIds()
                    .addAllFavoriteProductsIds(currentFavoriteProducts)
                    .build()

            }
        } catch (ioException: IOException) {
            Log.e("DataStore", "Failed to update favorite products", ioException)
        }
    }


}
